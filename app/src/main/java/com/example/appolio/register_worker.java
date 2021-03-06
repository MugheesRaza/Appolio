package com.example.appolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appolio.model.register_worker_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class register_worker extends AppCompatActivity {

    EditText email, username, password, cnic, phonenumber, confrimpassword;
    Spinner gender;
    Button btnRegister;
    DatabaseReference databaseReference;
    connectionDetector cd;
    ProgressDialog  pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_worker);
        cd = new connectionDetector(register_worker.this);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cnic = findViewById(R.id.cnic);
        phonenumber = findViewById(R.id.phone);
        confrimpassword = findViewById(R.id.cnfrmPassword);
        databaseReference = FirebaseDatabase.getInstance().getReference("WORKER");
        gender = findViewById(R.id.gender_spinner);

        pd = new ProgressDialog(register_worker.this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Items_array_gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        btnRegister = findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnected()){

                    pd.setCancelable(false);
                    pd.setMessage("Loading...");
                    pd.show();
                    check_cnic();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(register_worker.this);
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
        });
    }

    private void check_cnic(){


        String txt_cnic = cnic.getText().toString();
        databaseReference.orderByChild("cnic").equalTo(txt_cnic).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Toasty.error(getApplicationContext(),"This CNIC is already registered",Toasty.LENGTH_SHORT).show();
                    pd.dismiss();

                }else{
//                    addData();
                    check_email();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

              Toast.makeText(getApplicationContext(),"Record not exist ",Toast.LENGTH_SHORT).show();
                pd.dismiss();

            }
        });
    }

    private void check_email() {
        String txt_email = email.getText().toString();
        databaseReference.orderByChild("email").equalTo(txt_email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    pd.dismiss();
                    Toasty.error(getApplicationContext(),"This Email is already registered",Toasty.LENGTH_SHORT).show();
                }
                else {
                    addData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
            }
        });
    }

    private void addData() {
        String txtemail = email.getText().toString();
        String txtusername = username.getText().toString();
        String txtpassword = password.getText().toString();
        String txtcnic = cnic.getText().toString();
        String txtphonenumber = phonenumber.getText().toString();
        String txtcnfrimpassword = confrimpassword.getText().toString();
        String txtgender = gender.getSelectedItem().toString();
        String role = "Worker";
        String status = "In Progress";


        if (txtemail.equals("") || txtusername.equals("") || txtpassword.equals("") || txtcnfrimpassword.equals("") || txtphonenumber.equals("") || txtcnic.equals("")) {
            Toasty.error(getApplicationContext(), "Please enter all fields", Toasty.LENGTH_SHORT).show();
            pd.dismiss();
        } else if (!isEmailValid(txtemail)) {
            email.setError("Invalid Email");
            email.requestFocus();
            Toasty.error(getApplicationContext(), "Enter a valid Email", Toasty.LENGTH_SHORT).show();
            pd.dismiss();

        } else if (txtpassword.length() <= 7) {
            Toasty.error(getApplicationContext(), "Please enter password greater than 7 ", Toasty.LENGTH_LONG).show();

            pd.dismiss();
        }
        else if (!txtpassword.equals(txtcnfrimpassword)){
            Toasty.error(getApplicationContext(), "Please confirm password", Toasty.LENGTH_SHORT).show();

            pd.dismiss();
        }
        else if (txtcnic.length()>13 || txtcnic.length()<13) {

            Toasty.error(getApplicationContext(), "Invalid CNIC", Toasty.LENGTH_SHORT).show();
            pd.dismiss();
        }
        else if (txtphonenumber.length()>11 || txtphonenumber.length()<11){
            Toasty.error(getApplicationContext(), "Invalid phone number", Toasty.LENGTH_SHORT).show();

            pd.dismiss();
        }
        else {
            pd.dismiss();
            String id = databaseReference.push().getKey();
            register_worker_model worker_model = new register_worker_model(txtcnic, txtemail, txtgender, txtusername, txtpassword, txtphonenumber, txtcnfrimpassword, role, status);
            databaseReference.child(id).setValue(worker_model);

            AlertDialog.Builder builder = new AlertDialog.Builder(register_worker.this);
            builder.setMessage("Your profile is delivered to the administration just wait some time to approve your profile.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                            Intent i = new Intent(getApplicationContext(),worker_login.class);
                            startActivity(i);
                            finish();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.setTitle("Thank you");
            alert.show();
        }
    }

    boolean isEmailValid(CharSequence email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }
}
