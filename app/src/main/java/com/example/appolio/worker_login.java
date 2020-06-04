package com.example.appolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class worker_login extends AppCompatActivity {

    connectionDetector cd;
    Button btnLogin, btnRegister;
    TextView coor_login,change_passwword,forgotpassword;
    EditText email, password;
    String txtemail, txtpassword,cnic,useremail,username;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);

        mAuth = FirebaseAuth.getInstance();
        cd = new connectionDetector(worker_login.this);
        forgotpassword = findViewById(R.id.forgotpass);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        btnRegister = findViewById(R.id.register);
        change_passwword = findViewById(R.id.pass_change);
        reference = FirebaseDatabase.getInstance().getReference("WORKER");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_click();

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), register_worker.class);
                startActivity(i);
            }
        });
        change_passwword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),change_password.class);
                startActivity(i);
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),forgotPassword.class);
                startActivity(i);
            }
        });


    }


    public void button_click() {
        if (cd.isConnected()) {

            txtemail = email.getText().toString();
            txtpassword = password.getText().toString();
            if (txtemail.equals("") || txtpassword.equals("")) {
                Toasty.error(getApplicationContext(), "Please enter all fields", Toasty.LENGTH_SHORT).show();
            } else if (txtpassword.length() <= 6) {
                Toasty.error(getApplicationContext(), "Please enter password greater then 6 ", Toasty.LENGTH_LONG).show();
            } else if (!isEmailValid(txtemail)) {
                email.setError("Invalid Email");
                email.requestFocus();
                Toasty.error(getApplicationContext(), "Please enter valid email", Toasty.LENGTH_SHORT).show();
                return;
            } else {
                fetchCNIC();


            }


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(worker_login.this);
            builder.setMessage("You need to have Mobile Data or wifi to access this.Press Ok to Exit  !!!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.setTitle("Alert !!!");
            alert.show();
        }

    }

    private void fetchCNIC() {


        reference.orderByChild("email").equalTo(txtemail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if (snapshot.exists()){
                        cnic = snapshot.child("cnic").getValue().toString();
                        username= snapshot.child("name").getValue().toString();
                        useremail = snapshot.child("email").getValue().toString();
                        snapshot.getRef().child("password").setValue(txtpassword);
                        snapshot.getRef().child("repeat_password").setValue(txtpassword);
                       // Toast.makeText(getApplicationContext(),cnic,Toast.LENGTH_SHORT).show();

                        signin_wtih_firebase_auth();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void signin_wtih_firebase_auth() {
        final ProgressDialog pd = new ProgressDialog(worker_login.this);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        pd.show();

        mAuth.signInWithEmailAndPassword(txtemail, txtpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        Intent i = new Intent(getApplicationContext(), drawer_Activity.class);
                        i.putExtra("cnic",cnic);
                        i.putExtra("username",username);
                        i.putExtra("userEmail",useremail);
                        startActivity(i);
                        pd.dismiss();
                        finish();
                    } else {
                        pd.dismiss();
                        Toasty.info(getApplicationContext(), "Your account is not created by administration yet ", Toasty.LENGTH_LONG).show();
                    }
                } else {
                    pd.dismiss();
                    Toasty.error(getApplicationContext(), "Invalid Email or Password", Toasty.LENGTH_LONG).show();
                }
            }
        });

    }

    boolean isEmailValid(CharSequence email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }


}
