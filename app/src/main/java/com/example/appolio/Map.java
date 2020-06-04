package com.example.appolio;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appolio.model.child_location;
import com.example.appolio.model.model_child;
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
import com.google.firebase.database.ValueEventListener;

import org.joda.time.Years;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Calendar;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Location locations;
    Location current_location;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int request_code = 101;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        databaseReference = FirebaseDatabase.getInstance().getReference("CHILD RECORD");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    model_child loc = snapshot.getValue(model_child.class);
                    //Toast.makeText(getApplicationContext(),loc.getLatitude()+" "+loc.getLongitude(),Toast.LENGTH_LONG).show();
                    LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                    String DOB = loc.getDateofBirth();
                    String[] parts = DOB.split("-");
                    String  day = parts[0];
                    String month = parts[1];
                    String year = parts[2];
                    int age = getAge(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
                    //Toast.makeText(getApplicationContext(),years+"     "+ month+"      "+days,Toast.LENGTH_LONG).show();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Name: " + loc.getChildname()).snippet("Age: "+age)).showInfoWindow();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private int getAge(int year, int month, int day) {
//        //calculating age from dob
//        Calendar dob = Calendar.getInstance();
//        Calendar today = Calendar.getInstance();
//        dob.set(year, month, day);
//        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//        if (today.get(Calendar.DAY_OF_YEAR) > dob.get(Calendar.DAY_OF_YEAR)) {
//            age = 0;
//        }
//        return age;
//    }



    private int getAge(int year, int month, int day) {
        int age = 0;
        //calculating age from dob
        Calendar dob = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        dob.set(year, month, day);
        if (dob.after(now)) {
            age = 0;
        }
        int year1 = now.get(Calendar.YEAR);
        int year2 = dob.get(Calendar.YEAR);
        age = year1 - year2;
        int month1 = now.get(Calendar.MONTH);
        int month2 = dob.get(Calendar.MONTH);
        if (month2 > month1) {
            age--;
        }
        else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = dob.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age--;
            }
        }
        return age;
    }
}
