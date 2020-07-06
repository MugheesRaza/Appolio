package com.example.appolio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appolio.R;

import es.dmoral.toasty.Toasty;

public class send_mail extends Fragment {

    EditText editTextTo,editTextsubj,editTextmailtxt;
    Button btnsendmail;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_send_mai, container, false);

        editTextTo = v.findViewById(R.id.to);
        editTextsubj = v.findViewById(R.id.subject);
        editTextmailtxt = v.findViewById(R.id.txtmail);
        btnsendmail = v.findViewById(R.id.send);
        btnsendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMAIL();
            }
        });

        return v;
    }

    private void sendMAIL() {
        if (editTextTo.getText().toString().equals("")||editTextsubj.getText().toString().equals("")||editTextmailtxt.getText().toString().equals(""))
        {
            Toasty.error(getContext(),"Please enter all fields",Toasty.LENGTH_SHORT).show();
        }
        else if (!isEmailValid(editTextTo.getText().toString())){
            editTextTo.setError("Invalid Email");
            editTextTo.requestFocus();
            Toasty.error(getContext(), "Please enter valid email", Toasty.LENGTH_SHORT).show();
            return;
        }
        else {
            String mailList = editTextTo.getText().toString();
            String[] mails = mailList.split(",");

            String subject = editTextsubj.getText().toString();
            String masgtxt = editTextmailtxt.getText().toString();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL,mails);
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,masgtxt);

            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent,"Choose an email client"));
        }


    }
    boolean isEmailValid(CharSequence email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }
}
