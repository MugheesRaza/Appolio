package com.example.appolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.Manifest.permission;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appolio.model.child_location;
import com.example.appolio.model.model_child;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class child_Record extends AppCompatActivity {

    EditText fathername, mothername, chidname, phonenumber, address, childBfom, gardianCNIC, gardianEmail;
    Spinner child_gender, day, mnth, year;
    Button addrecord;
    String company_name, vaccine_name, vaccine_quality, vaccinetype, dateOfBirtth, cnic, vaccine_id;

    double latitude;
    double longitude;
    ProgressDialog pd;
    String txt_childgender, txt_day, txt_mnth, txt_year, txt_fathername, txt_mothername, txt_childname, txt_phone, txt_address, txt_bform, txt_Cnic, txt_email;
    DatabaseReference databaseReference, databaseReferencechild, reference, workerref;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private GoogleMap mMap;
    Location current_location;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int request_code = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child__record);
        pd = new ProgressDialog(child_Record.this);
        pd.setCancelable(false);
        pd.setMessage("Loading...");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(200000 * 1000);


        company_name = getIntent().getStringExtra("c_name");
        vaccine_name = getIntent().getStringExtra("v_name");
        vaccine_quality = getIntent().getStringExtra("v_quality");
        vaccinetype = getIntent().getStringExtra("v_type");
        cnic = getIntent().getStringExtra("cnic");
        vaccine_id = getIntent().getStringExtra("vaccine_id");

        fathername = findViewById(R.id.father_name);
        chidname = findViewById(R.id.child_name);
        mothername = findViewById(R.id.mother_name);
        phonenumber = findViewById(R.id.parent_phonenumber);
        address = findViewById(R.id.child_address);
        childBfom = findViewById(R.id.child_bform);
        gardianCNIC = findViewById(R.id.parent_cnic);
        gardianEmail = findViewById(R.id.parent_email);
        addrecord = findViewById(R.id.btn_add_childRecord);


        day = (Spinner) findViewById(R.id.date_birth);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Items_array_day, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(adapter1);

        mnth = (Spinner) findViewById(R.id.month_birth);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Items_array_month, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mnth.setAdapter(adapter2);

        year = (Spinner) findViewById(R.id.year_birth);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Items_array_year, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapter3);

        child_gender = (Spinner) findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Items_array_child_gender, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        child_gender.setAdapter(adapter4);

        databaseReferencechild = FirebaseDatabase.getInstance().getReference("CHILD LOCATION");
        databaseReference = FirebaseDatabase.getInstance().getReference("CHILD RECORD");
        reference = FirebaseDatabase.getInstance().getReference("VACCINE");
        workerref = FirebaseDatabase.getInstance().getReference("WORKER");


        addrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLastLocation();


            }
        });
    }

    private void checkValidations() {
        txt_childgender = child_gender.getSelectedItem().toString();
        txt_day = day.getSelectedItem().toString();
        txt_mnth = mnth.getSelectedItem().toString();
        txt_year = year.getSelectedItem().toString();
        dateOfBirtth = txt_day + "-" + txt_mnth + "-" + txt_year;
        txt_fathername = fathername.getText().toString();
        txt_mothername = mothername.getText().toString();
        txt_childname = chidname.getText().toString();
        txt_phone = phonenumber.getText().toString();
        txt_address = address.getText().toString();
        txt_bform = childBfom.getText().toString();
        txt_Cnic = gardianCNIC.getText().toString();
        txt_email = gardianEmail.getText().toString();
        int year, month, days;
        year = Integer.parseInt(txt_year);
        month = Integer.parseInt(txt_mnth);
        days = Integer.parseInt(txt_day);
        int age = getAge(year, month, days);
        if (txt_childgender.equals("") || txt_day.equals("") || txt_mnth.equals("") || txt_year.equals("") || txt_fathername.equals("") || txt_mothername.equals("") || txt_childname.equals("") || txt_phone.equals("") || txt_address.equals("") || txt_bform.equals("") || txt_Cnic.equals("") || txt_email.equals("")) {
            Toasty.info(getApplicationContext(), "Please  enter all fields", Toasty.LENGTH_SHORT).show();
           //Toasty.info(getApplicationContext(), age + "", Toasty.LENGTH_SHORT).show();
        } else if (txt_Cnic.length() < 13 || txt_Cnic.length() > 13) {
            Toasty.error(getApplicationContext(), "Please enter valid cnic", Toasty.LENGTH_SHORT).show();
        } else if (txt_phone.length() > 11 || txt_phone.length() < 11) {
            phonenumber.setError("Invalid phone number");
            phonenumber.requestFocus();
            Toasty.error(getApplicationContext(), "Please enter valid phone number", Toasty.LENGTH_SHORT).show();
            return;
        } else if (txt_address.length() < 15) {
            Toasty.error(getApplicationContext(), "Address length greater than 15 required", Toasty.LENGTH_SHORT).show();
        } else if (txt_address.length() > 50) {
            Toasty.error(getApplicationContext(), "Address length less than 50 required", Toasty.LENGTH_SHORT).show();

        } else if (txt_bform.length() < 13 || txt_bform.length() > 13) {
            childBfom.setError("Invalid Bform");
            childBfom.requestFocus();
            Toasty.error(getApplicationContext(), "Please enter valid Bform number", Toasty.LENGTH_SHORT).show();
            return;
        } else if (!isEmailValid(txt_email)) {
            gardianEmail.setError("Invalid Email");
            gardianEmail.requestFocus();
            Toasty.error(getApplicationContext(), "Please enter valid email", Toasty.LENGTH_SHORT).show();
            return;
        } else if (age == 0 || age <0) {
            Toasty.error(getApplicationContext(), "Minimum age should be 1 year", Toasty.LENGTH_SHORT).show();

        } else if (age == 6) {
            Toasty.error(getApplicationContext(), "Minimum age should be 5 year", Toasty.LENGTH_SHORT).show();
        } else {
            pd.show();
            //Toast.makeText(getApplicationContext(), age + "", Toast.LENGTH_SHORT).show();
            addChildData();
            //   fetchLastLocation();


        }

    }

    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION}, request_code);
            return;
        }
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    current_location = location;
//                    double lati =  current_location.getLatitude();
//                    double longi = current_location.getLongitude();

                    latitude = current_location.getLatitude();
                    longitude = current_location.getLongitude();
                    checkValidations();
                    //Toast.makeText(getApplicationContext(), current_location.getLatitude() + "       " + current_location.getLongitude(), Toast.LENGTH_LONG).show();
                } else {
                    locationCallback = new LocationCallback() {

                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if (locationResult == null) {
                                Toast.makeText(getApplicationContext(), "Location null", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (Location location : locationResult.getLocations()) {

                                if (location != null) {
                                    Toast.makeText(getApplicationContext(), location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Your Location is null", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    };

                    Toasty.info(getApplicationContext(), "Please turn on your location to get the child current location", Toasty.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addChildData() {

//        String txt_childgender = child_gender.getSelectedItem().toString();
//        String txt_day = day.getSelectedItem().toString();
//        String txt_mnth = mnth.getSelectedItem().toString();
//        String txt_year = year.getSelectedItem().toString();
//        dateOfBirtth = txt_day + "-" + txt_mnth + "-" + txt_year;
//        String txt_fathername = fathername.getText().toString();
//        String txt_mothername = mothername.getText().toString();
//        String txt_childname = chidname.getText().toString();
//        String txt_phone = phonenumber.getText().toString();
//        String txt_address = address.getText().toString();
//        String txt_bform = childBfom.getText().toString();
//        String txt_Cnic = gardianCNIC.getText().toString();
//        String txt_email = gardianEmail.getText().toString();

        String id = databaseReference.push().getKey();
        model_child modelchild = new model_child(txt_fathername, txt_mothername, txt_childname, txt_phone, txt_address, txt_childgender, dateOfBirtth, txt_bform, txt_Cnic, txt_email, latitude, longitude, company_name, vaccine_name, vaccine_quality, vaccinetype, vaccine_id, cnic);
        databaseReference.child(id).setValue(modelchild);

        reference.orderByChild("vaccine_id").equalTo(vaccine_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().child("status").setValue("used-" + cnic);
                    passData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        databaseReferencechild.orderByChild("latitude").equalTo(latitude).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    if (dataSnapshot.exists()){
//                        Toasty.success(getApplicationContext(), "Location already exist", Toasty.LENGTH_SHORT).show();
//                    }
//                    else {
//                        String locid = databaseReferencechild.push().getKey();
//                        child_location loc = new child_location(latitude,longitude,txt_childname,txt_address,vaccine_name);
//                        databaseReferencechild.child(locid).setValue(loc);
//                    }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        String locid = databaseReferencechild.push().getKey();
//        child_location location = new child_location(latitude, longitude);
//        databaseReferencechild.child(locid).setValue(location);

        // Toasty.success(getApplicationContext(),latitude+ " "+longitude,Toasty.LENGTH_SHORT).show();

    }

    private void passData() {
        workerref.orderByChild("cnic").equalTo(cnic).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Intent i = new Intent(getApplicationContext(), drawer_Activity.class);
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    i.putExtra("username", name);
                    i.putExtra("cnic", cnic);
                    i.putExtra("userEmail", email);

                    pd.dismiss();
                    startActivity(i);
                    Toasty.success(getApplicationContext(), "Child Data add successfully ", Toasty.LENGTH_SHORT).show();
                    finish();
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

    //    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        LatLng latitiude_logitude = new LatLng(current_location.getLatitude(),current_location.getLongitude());
//        Toast.makeText(getApplicationContext(),current_location.getLatitude()+"   "+current_location.getLongitude(),Toast.LENGTH_SHORT).show();
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case request_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }


    }


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

//
//    public static int getAge(String date) {
//
//        int age = 0;
//        try {
//            Date date1 = dateFormat.parse(date);
//            Calendar now = Calendar.getInstance();
//            Calendar dob = Calendar.getInstance();
//            dob.setTime(date1);
//            if (dob.after(now)) {
//                throw new IllegalArgumentException("Can't be born in the future");
//            }
//            int year1 = now.get(Calendar.YEAR);
//            int year2 = dob.get(Calendar.YEAR);
//            age = year1 - year2;
//            int month1 = now.get(Calendar.MONTH);
//            int month2 = dob.get(Calendar.MONTH);
//            if (month2 > month1) {
//                age--;
//            } else if (month1 == month2) {
//                int day1 = now.get(Calendar.DAY_OF_MONTH);
//                int day2 = dob.get(Calendar.DAY_OF_MONTH);
//                if (day2 > day1) {
//                    age--;
//                }
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return age ;

}

