package com.example.appolio;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appolio.model.vaccine;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class vaccine_list extends RecyclerView.Adapter<vaccine_list.ViewHolder> {


    Context context;
    ArrayList<vaccine> vaccines;
    String cnic;


    public vaccine_list(Context context, ArrayList<vaccine> vaccines,String cnic) {
        this.context = context;
        this.vaccines = vaccines;
        this.cnic = cnic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final vaccine vc  = vaccines.get(position);
        holder.company_name.setText("Company: "+vaccines.get(position).getCompany_name());
        holder.vaccine_name.setText("Name: "+vaccines.get(position).getVaccine_name());
        holder.vaccine_Quality.setText("Quality: "+vaccines.get(position).getVaccine_quality());
        holder.vaccine_Type.setText("Type: "+vaccines.get(position).getVaccine_type());



       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               final AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setMessage("Are You sure you want to use this vaccine");
               builder.setCancelable(true);
               builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       holder.reference.orderByChild("vaccine_id").equalTo(vc.getVaccine_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                                   String company = (String) snapshot.child("company_name").getValue();
                                   String v_name = (String) snapshot.child("vaccine_name").getValue();
                                   String v_quality = (String) snapshot.child("vaccine_quality").getValue();
                                   String v_type = (String) snapshot.child("vaccine_type").getValue();
                                   String v_id = (String) snapshot.child("vaccine_id").getValue();

                                   //Toast.makeText(context,company+"  "+v_name+"  "+v_quality+"  "+v_type,Toast.LENGTH_LONG).show();
                                   //Toast.makeText(context,cnic,Toast.LENGTH_SHORT).show();
                                   Intent i = new Intent(context,child_Record.class);
                                   i.putExtra("c_name",company);
                                   i.putExtra("v_name",v_name);
                                   i.putExtra("v_quality",v_quality);
                                   i.putExtra("v_type",v_type);
                                   i.putExtra("cnic",cnic);
                                   i.putExtra("vaccine_id",v_id);
                                   context.startActivity(i);
                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                   }
               });

               builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });
               AlertDialog alertDialog = builder.create();
               alertDialog.show();
           }
       });


    }

    @Override
    public int getItemCount() {
        return vaccines.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView company_name;
        TextView vaccine_name;
        TextView vaccine_Quality;
        TextView vaccine_Type;
        public RelativeLayout relativeLayout;
        DatabaseReference reference;
        String na;

        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relative);
            company_name = itemView.findViewById(R.id.cmpy_name);
            vaccine_name = itemView.findViewById(R.id.vacine_name);
            vaccine_Quality = itemView.findViewById(R.id.vacine_quality);
            vaccine_Type = itemView.findViewById(R.id.vacine_type);
            reference = FirebaseDatabase.getInstance().getReference("VACCINE");


        }


//    public vaccine_list(Activity context, List<vaccine> vaccineList) {
//
//        super( context,R.layout.list_layout,vaccineList);
//        this.mcontext =  context;
//        this.vaccineList = vaccineList;
//    }



    }
}
