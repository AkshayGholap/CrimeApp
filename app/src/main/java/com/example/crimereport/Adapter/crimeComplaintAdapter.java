package com.example.crimereport.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crimereport.*;
import com.example.crimereport.R;
import com.example.crimereport.model.*;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class crimeComplaintAdapter extends RecyclerView.Adapter<crimeComplaintAdapter.MyViewHolder> {


    Context context;
    ArrayList<crimeComplaintModel> crimeComplaintModelArrayList;

    public crimeComplaintAdapter(crimeComplaintDashboard crimeComplaintDashboard, ArrayList<crimeComplaintModel> crimeComplaintModelArrayList) {

        this.context = crimeComplaintDashboard;
        this.crimeComplaintModelArrayList = crimeComplaintModelArrayList;

    }




    @NonNull
    @Override
    public crimeComplaintAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crime_complaint_list_layout,parent,false);

        return new crimeComplaintAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull crimeComplaintAdapter.MyViewHolder holder, int position) {


        if(crimeComplaintModelArrayList.isEmpty() ){



        }else {
            holder.compname.setText(crimeComplaintModelArrayList.get(position).getComplainerName().toUpperCase());
            holder.victimname.setText(crimeComplaintModelArrayList.get(position).getVictimName().toUpperCase());
            holder.victimage.setText(crimeComplaintModelArrayList.get(position).getVictimAge());
           holder.status.setText(crimeComplaintModelArrayList.get(position).getStatus().toUpperCase());
            holder.victimgender.setText(crimeComplaintModelArrayList.get(position).getVictimGender().toUpperCase());
            holder.phone.setText("Phone :"+crimeComplaintModelArrayList.get(position).getPhone());
            holder.crimeDate.setText("Date :"+crimeComplaintModelArrayList.get(position).getCrimeRegisterDate());
            holder.location.setText("Place :"+crimeComplaintModelArrayList.get(position).getLocation().toUpperCase());
            holder.crimeId.setText("Crime | Crime Id : "+crimeComplaintModelArrayList.get(position).getCrimeId());

            Glide.with(context)
                    .load(Uri.parse(crimeComplaintModelArrayList.get(position).getImageUri()))
                    .placeholder(R.drawable.placeholder_images)
                    .error(R.drawable.baseline_error_outline_24)
                    .timeout(10000)
                    .into(holder.crimeImage);


        }





    }

    @Override
    public int getItemCount() {
        return crimeComplaintModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircularImageView crimeImage;
        TextView victimage,victimgender,status,phone,compname,victimname,crimeId,crimeDate, location ;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);



            crimeImage = itemView.findViewById(R.id.crimeImage_CrimeComp);
            victimage = itemView.findViewById(R.id.crimeAge_CrimeComp);
            victimgender = itemView.findViewById(R.id.crimegender_CrimeComp);
            status = itemView.findViewById(R.id.crimeStatus_CrimeComp);
            phone   = itemView.findViewById(R.id.crimePhone_CrimeComp);
            victimname = itemView.findViewById(R.id.victimName_CrimeComp);
            compname = itemView.findViewById(R.id.crimeComplainerName_CrimeComp);
            crimeId = itemView.findViewById(R.id.crtimeId_CrimeComp);
            crimeDate = itemView.findViewById(R.id.crimeDate_CrimeComp);
            location = itemView.findViewById(R.id.crimeLocation_CrimeComp);



        }
    }
}
