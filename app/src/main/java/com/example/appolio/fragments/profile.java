package com.example.appolio.fragments;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appolio.R;
import com.example.appolio.child_vaccine;
import com.example.appolio.drawer_Activity;
import com.example.appolio.model.child_location;
import com.example.appolio.model.vaccine;
import com.example.appolio.vaccine_list;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profile extends Fragment  {

    TextView email ,cnictxt,name;

    RecyclerView recyclerView;
    ArrayList<vaccine> vaccineList;
    DatabaseReference reference;
    drawer_Activity drawer_activity;
    String cnic;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_profile, container, false);

        name = v.findViewById(R.id.username_profile);
        cnictxt = v.findViewById(R.id.cnictxt);
        email = v.findViewById(R.id.emailtxt);
        drawer_activity = (drawer_Activity) getActivity();

        cnic = drawer_activity.getCnic();
        vaccineList = new ArrayList<vaccine>();
        reference = FirebaseDatabase.getInstance().getReference("VACCINE");
        name.setText("Welcome "+drawer_activity.getUsername());
        cnictxt.setText(drawer_activity.getCnic());
        email.setText(drawer_activity.getEmial());
        recyclerView  = v.findViewById(R.id.workerVaccine);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Query query = FirebaseDatabase.getInstance().getReference("VACCINE")
                .orderByChild("status").equalTo("assigned-"+drawer_activity.getCnic());
        query.addListenerForSingleValueEvent(valueEventListener);


        return v;
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            vaccineList.clear();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                vaccine vaccine = snapshot.getValue(vaccine.class);
                vaccineList.add(vaccine);
            }
            vaccine_list adapter = new vaccine_list(getContext(), vaccineList,cnic);
            recyclerView.setAdapter(adapter);


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
