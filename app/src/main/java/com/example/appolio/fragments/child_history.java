package com.example.appolio.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appolio.R;
import com.example.appolio.child_vaccine;
import com.example.appolio.drawer_Activity;
import com.example.appolio.history_adapter;
import com.example.appolio.model.child_location;
import com.example.appolio.model.history;
import com.example.appolio.model.model_child;
import com.example.appolio.model.vaccine;
import com.example.appolio.vaccine_list;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static androidx.core.content.ContextCompat.getSystemService;

public class child_history extends Fragment {

//
//    ListView listViewVaccine;
//    List<vaccine> vaccineList;
//    DatabaseReference reference;

//    drawer_Activity drawer_activity;
//    String cnic;

    EditText fathername, mothername, chidname, phonenumber, address, childBfom, gardianCNIC, gardianEmail;
    Spinner child_gender, day, mnth, year;
    Button addrecord, assignvaccine;
    LocationManager locationManager;
    String longitude, latitude;
    FusedLocationProviderClient mFusedLocationClient;
    Location current_location;

    drawer_Activity activity;
    String cnic;

    RecyclerView recyclerView;
    ArrayList<history> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_childrecord, container, false);

        list = new ArrayList<>();
        activity = (drawer_Activity) getActivity();

        cnic = activity.getCnic();

        recyclerView = v.findViewById(R.id.childRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Query query = FirebaseDatabase.getInstance().getReference("CHILD RECORD")
                .orderByChild("workerCnic").equalTo(cnic);
        query.addListenerForSingleValueEvent(valueEventListener);

        return v;
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            list.clear();

            for ( DataSnapshot snapshot:dataSnapshot.getChildren()){
                history h = snapshot.getValue(history.class);
                list.add(h);
            }
            history_adapter adapter = new history_adapter(getContext(),list);
            recyclerView.setAdapter(adapter);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

//    public void showUpdateDialogue(final String id, final String childname, final String fathername, final String mothername, final String address){
//        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
//        LayoutInflater inflater = getLayoutInflater();
//
//        final View dialogueView = inflater.inflate(R.layout.update_dialogue,null);
//        final EditText chname,fhname,moname,adres;
//        final Button btnupdate;
//
//        chname = dialogueView.findViewById(R.id.etname);
//        fhname = dialogueView.findViewById(R.id.etfather);
//        moname = dialogueView.findViewById(R.id.etmother);
//        adres = dialogueView.findViewById(R.id.etaddress);
//        btnupdate = dialogueView.findViewById(R.id.btnupdtae);
//
//        btnupdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String txtchaname = chname.getText().toString();
//                String txtfname = fhname.getText().toString();
//                String txtmoname = moname.getText().toString();
//                String txtadress = adres.getText().toString();
//                if (txtchaname.equals("")||txtfname.equals("")||txtmoname.equals("")||txtadress.equals("")){
//                    Toasty.error(getContext(),"Please Enter all fields",Toasty.LENGTH_SHORT).show();
//
//
//                }
//                else {
//
//                    updateRecord(id,childname,fathername,mothername,address);
//                }
//            }
//        });
//
//        dialogueBuilder.setTitle("Update child record");
//
//
//        AlertDialog dialog = dialogueBuilder.create();
//        dialog.show();
//
//
//    }
//
//    private void updateRecord(String id, String childname, String fathername, String mothername, String address) {
//
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CHILD RECORD").child(id);
//
//    }

}
