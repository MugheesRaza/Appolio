package com.example.appolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class forgotPassword extends AppCompatActivity {
    EditText email;
    Button emailvarify;
    String txtEmail;
    connectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);

        cd = new connectionDetector(getApplicationContext());
        emailvarify = findViewById(R.id.verify);
        emailvarify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckMail();
            }
        });

    }

    private void CheckMail() {
        if (cd.isConnected()){
            txtEmail = email.getText().toString();

            if (txtEmail.equals("")){
                Toasty.error(getApplicationContext(), "Please enter Email", Toasty.LENGTH_LONG).show();
            }
            else if (!isEmailValid(txtEmail)){
                email.setError("Invalid Email");
                email.requestFocus();
                Toasty.error(getApplicationContext(), "Please enter valid email", Toasty.LENGTH_SHORT).show();
                return;
            }

            else {
                final ProgressDialog pd = new ProgressDialog(forgotPassword.this);
                pd.setCancelable(false);
                pd.setMessage("Loading...");
                pd.show();
                FirebaseAuth.getInstance().sendPasswordResetEmail(txtEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toasty.info(getApplicationContext(), "Please go to this email and make a new password", Toasty.LENGTH_LONG).show();

                        }
                        else {
                            pd.dismiss();
                            Toasty.error(getApplicationContext(), "Invalid email", Toasty.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(forgotPassword.this);
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


    boolean isEmailValid(CharSequence email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }
}
