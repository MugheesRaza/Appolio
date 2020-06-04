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

public class change_password extends AppCompatActivity {

    EditText email, pass, newpass, cnfrmpass;
    Button btnChangePassword;
    String txtemail, txtpass, txtnewpass, txtcnfrimpassword;

    connectionDetector cd;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        cd = new connectionDetector(change_password.this);
        newpass = findViewById(R.id.newpassword);
        cnfrmpass = findViewById(R.id.cnfpassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        reference = FirebaseDatabase.getInstance().getReference("WORKER");

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckRecord();
            }
        });
    }

    private void CheckRecord() {
        if (cd.isConnected()) {

            final ProgressDialog pd = new ProgressDialog(change_password.this);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();


            txtemail = email.getText().toString();
            txtpass = pass.getText().toString();
            txtnewpass = newpass.getText().toString();
            txtcnfrimpassword = cnfrmpass.getText().toString();

            if (txtemail.equals("") || txtpass.equals("") || txtnewpass.equals("") || txtcnfrimpassword.equals("")) {
                Toasty.error(getApplicationContext(), "Enter All Fields", Toasty.LENGTH_SHORT).show();
                pd.dismiss();

            } else if (!isEmailValid(txtemail)) {
                email.setError("Invalid Email");
                email.requestFocus();
                Toasty.error(getApplicationContext(), "Please enter valid email", Toasty.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            } else if (txtpass.length() <= 6 || txtnewpass.length() <= 6 || txtcnfrimpassword.length() <= 6) {
                Toasty.error(getApplicationContext(), "Please enter password greater then 6 ", Toasty.LENGTH_LONG).show();
                pd.dismiss();
            } else if (!txtnewpass.equals(txtcnfrimpassword)) {
                Toasty.error(getApplicationContext(), "Please confirm your password", Toasty.LENGTH_SHORT).show();
                pd.dismiss();
            } else {
//                Toasty.error(getApplicationContext(), "Do nothing", Toasty.LENGTH_SHORT).show();

                mAuth.signInWithEmailAndPassword(txtemail,txtpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser!=null){
                                firebaseUser.updatePassword(txtnewpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            openworker_login();
                                            Toasty.success(getApplicationContext(), "Password Updated successfully", Toasty.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(),worker_login.class);
                                            pd.dismiss();

                                            startActivity(i);
                                            finish();
                                        }
                                        else {
                                            Toasty.error(getApplicationContext(), "Error : Password not updated", Toasty.LENGTH_SHORT).show();

                                            pd.dismiss();
                                        }
                                    }
                                });
                            }
                            else {
                                pd.dismiss();
                                Toasty.error(getApplicationContext(), "Invalid email or password", Toasty.LENGTH_SHORT).show();


                            }

                        }
                        else {
                            pd.dismiss();
                            Toasty.error(getApplicationContext(), "Invalid email or password", Toasty.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(change_password.this);
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

    private void openworker_login() {
        reference.orderByChild("email").equalTo(txtemail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    snapshot.getRef().child("password").setValue(txtnewpass);
                    snapshot.getRef().child("repeat_password").setValue(txtcnfrimpassword);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    boolean isEmailValid(CharSequence email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }


}
