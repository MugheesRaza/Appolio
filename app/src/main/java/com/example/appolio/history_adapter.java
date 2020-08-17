package com.example.appolio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appolio.model.history;
import com.example.appolio.model.vaccine;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class history_adapter extends RecyclerView.Adapter<history_adapter.Viewholder> {

    Context context;
    ArrayList<history> childHistory;

    public history_adapter(Context context, ArrayList<history> childHistory) {
        this.context = context;
        this.childHistory = childHistory;
    }

    @NonNull
    @Override
    public history_adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new history_adapter.Viewholder(LayoutInflater.from(context).inflate(R.layout.history_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final history_adapter.Viewholder holder, int position) {

        final history h1 = childHistory.get(position);
        holder.childname.setText("Child name :"+childHistory.get(position).getChildname());
        holder.fathername.setText("Father name :"+childHistory.get(position).getFathername());
        holder.mothername.setText("Mother name :"+childHistory.get(position).getMothername());
        holder.address.setText("Address :"+childHistory.get(position).getAddress());
        holder.vaccinename.setText("Vaccine name: "+childHistory.get(position).getVaccine_name());


       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(context);

               dialogueBuilder.setTitle("Confirm");
               dialogueBuilder.setMessage("Are you sure you want to update the record?");
               dialogueBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                       Intent i = new Intent(context,update_child_record.class);
                       String cnic = h1.getCninc();
                       String name = h1.getChildname();
                       i.putExtra("childname",name);
                       i.putExtra("fathercnic",cnic);
                       context.startActivity(i);

                   }
               });
               dialogueBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });

               AlertDialog alert = dialogueBuilder.create();
               alert.show();
           }
       });
    }

    @Override
    public int getItemCount() {
        return childHistory.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        TextView childname,fathername,mothername,address,vaccinename;

        public RelativeLayout relativeLayout;
        DatabaseReference reference;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.rel);
            childname = itemView.findViewById(R.id.kid_name);
            fathername = itemView.findViewById(R.id.kidfather_name);
            mothername = itemView.findViewById(R.id.kidMothername);
            address = itemView.findViewById(R.id.kidaddress);
            vaccinename = itemView.findViewById(R.id.kid_vaccine_name);
            reference = FirebaseDatabase.getInstance().getReference("CHILD RECORD");
        }
    }


}
