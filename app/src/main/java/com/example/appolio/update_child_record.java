package com.example.appolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class update_child_record extends AppCompatActivity {

    EditText chname, fhname, moname, adres,childBfom, gardianCNIC, gardianEmail,phonenumber;
    Button btnupdate;
    String fathercnic, childname;

    String txtchaname, txtfname, txtmoname, txtadress,txt_bform, txt_Cnic, txt_email,txt_phone;
    DatabaseReference ref;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_child_record);

        pd = new ProgressDialog(update_child_record.this);
        pd.setCancelable(false);
        pd.setMessage("Loading...");

        fathercnic = getIntent().getStringExtra("fathercnic");
        childname = getIntent().getStringExtra("childname");
        chname = findViewById(R.id.etname);
        fhname = findViewById(R.id.etfather);
        moname = findViewById(R.id.etmother);
        adres = findViewById(R.id.etaddress);
        btnupdate = findViewById(R.id.btnupdtae);
        childBfom = findViewById(R.id.child_bform);
        gardianCNIC = findViewById(R.id.parent_cnic);
        gardianEmail = findViewById(R.id.parent_email);
        phonenumber = findViewById(R.id.parent_phonenumber);
        ref = FirebaseDatabase.getInstance().getReference("CHILD RECORD");

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidations();
            }
        });
    }

    private void checkValidations() {
        txtchaname = chname.getText().toString();
        txtfname = fhname.getText().toString();
        txtmoname = moname.getText().toString();
        txtadress = adres.getText().toString();
        txt_bform = childBfom.getText().toString();
        txt_Cnic = gardianCNIC.getText().toString();
        txt_email = gardianEmail.getText().toString();
        txt_phone = phonenumber.getText().toString();
        if (txtchaname.equals("") || txtfname.equals("") ||  txtmoname.equals("") || txtadress.equals("")) {
            Toasty.error(getApplicationContext(), "Please Enter any fields", Toasty.LENGTH_SHORT).show();
        }
        else if (txtadress.length() < 15) {
            Toasty.error(getApplicationContext(), "Address length greater than 15 required", Toasty.LENGTH_SHORT).show();
        } else if (txtadress.length() > 50) {
            Toasty.error(getApplicationContext(), "Address length less than 50 required", Toasty.LENGTH_SHORT).show();
        }
        else if (txt_Cnic.length() < 13 || txt_Cnic.length() > 13) {
            Toasty.error(getApplicationContext(), "Please enter valid cnic", Toasty.LENGTH_SHORT).show();
        }
        else if (txt_phone.length() > 11 || txt_phone.length() < 11) {
            phonenumber.setError("Invalid phone number");
            phonenumber.requestFocus();
            Toasty.error(getApplicationContext(), "Please enter valid phone number", Toasty.LENGTH_SHORT).show();
            return;
        }else if (txt_bform.length() < 13 || txt_bform.length() > 13) {
            childBfom.setError("Invalid Bform");
            childBfom.requestFocus();
            Toasty.error(getApplicationContext(), "Please enter valid Bform number", Toasty.LENGTH_SHORT).show();
            return;
        } else if (!isEmailValid(txt_email)) {
            gardianEmail.setError("Invalid Email");
            gardianEmail.requestFocus();
            Toasty.error(getApplicationContext(), "Please enter valid email", Toasty.LENGTH_SHORT).show();
            return;
        }
        else {
            pd.show();
            ref.orderByChild("childname").equalTo(childname).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                       if (snapshot.exists()){
                           checkFathercnic();
                       }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void checkFathercnic() {
        ref.orderByChild("cninc").equalTo(fathercnic).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.exists()){
                        snapshot.getRef().child("childname").setValue(txtchaname);
                        snapshot.getRef().child("fathername").setValue(txtfname);
                        snapshot.getRef().child("mothername").setValue(txtmoname);
                        snapshot.getRef().child("address").setValue(txtadress);
                        snapshot.getRef().child("bForm").setValue(txt_bform);
                        snapshot.getRef().child("cninc").setValue(txt_Cnic);
                        snapshot.getRef().child("email").setValue(txt_email);
                        snapshot.getRef().child("phonenumber").setValue(txt_phone);
                        pd.dismiss();
                        Toasty.success(getApplicationContext(),"Record updated successfully",Toasty.LENGTH_SHORT).show();

//                        Intent i = new Intent(getApplicationContext(),drawer_Activity.class);
//                        startActivity(i);
//                        finish();
                    }
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
