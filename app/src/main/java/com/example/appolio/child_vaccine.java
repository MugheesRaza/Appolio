package com.example.appolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.appolio.model.vaccine;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class child_vaccine extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<vaccine> vaccineList;
    DatabaseReference reference;
    int pos;


    drawer_Activity drawer_activity;
    String cnic;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_vaccine);

        cnic = getIntent().getStringExtra("cnic");
        vaccineList = new ArrayList<vaccine>();
        reference = FirebaseDatabase.getInstance().getReference("VACCINE");

        recyclerView = findViewById(R.id.vaccineListchild);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



//         drawer_activity = (drawer_Activity) getActivity();
//        cnic = drawer_activity.getCnic();

//        Toast.makeText(getContext(),cnic,Toast.LENGTH_SHORT).show();
//
        Query query = FirebaseDatabase.getInstance().getReference("VACCINE")
                .orderByChild("status").equalTo("assigned-"+cnic);
        query.addListenerForSingleValueEvent(valueEventListener);



    }

        ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            vaccineList.clear();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                vaccine vaccine = snapshot.getValue(vaccine.class);
                vaccineList.add(vaccine);
            }
            vaccine_list adapter = new vaccine_list(child_vaccine.this, vaccineList,cnic);
            recyclerView.setAdapter(adapter);


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


}
