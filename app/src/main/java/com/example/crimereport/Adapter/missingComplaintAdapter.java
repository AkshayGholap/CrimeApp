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
import com.example.crimereport.MissingComplaintDashboard;
import com.example.crimereport.R;
import com.example.crimereport.model.missingComplaintModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class missingComplaintAdapter extends RecyclerView.Adapter<missingComplaintAdapter.MyViewHolder > {

 Context context;
 ArrayList<missingComplaintModel> missingComplaintModelArrayList;

    public missingComplaintAdapter(MissingComplaintDashboard missingComplaintDashboard, ArrayList<missingComplaintModel> missingComplaintModelArrayList) {

     this.context = missingComplaintDashboard;
     this.missingComplaintModelArrayList = missingComplaintModelArrayList;

 }

    @NonNull
    @Override
    public missingComplaintAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.missing_complaint_list_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull missingComplaintAdapter.MyViewHolder holder, int position) {


        if(missingComplaintModelArrayList.isEmpty() ){



        }else {
            holder.name.setText(missingComplaintModelArrayList.get(position).getName().toUpperCase());
            holder.age.setText(missingComplaintModelArrayList.get(position).getAge());
            holder.height.setText(missingComplaintModelArrayList.get(position).getHeight());
            holder.status.setText(missingComplaintModelArrayList.get(position).getStatus().toUpperCase());
            holder.gender.setText(missingComplaintModelArrayList.get(position).getGender().toUpperCase());
            holder.phone.setText("Phone :"+missingComplaintModelArrayList.get(position).getPhone());
            holder.crimeDate.setText("Date :"+missingComplaintModelArrayList.get(position).getCrimeRegisterDate());
            holder.location.setText("Place :"+missingComplaintModelArrayList.get(position).getLocation().toUpperCase());
            holder.crimeId.setText("Missing | Crime Id : "+missingComplaintModelArrayList.get(position).getCrimeId());

            Glide.with(context)
                    .load(Uri.parse(missingComplaintModelArrayList.get(position).getImageUri()))
                    .placeholder(R.drawable.placeholder_images)
                    .error(R.drawable.baseline_error_outline_24)
                    .timeout(10000)
                    .into(holder.crimeImage);


        }


    }

    @Override
    public int getItemCount() {
        return missingComplaintModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


      CircularImageView crimeImage;
      TextView age,height,gender,status,phone,name,crimeId,crimeDate, location ;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            crimeImage = itemView.findViewById(R.id.crimeImage_missingComp);
            age = itemView.findViewById(R.id.crimeAge_missingComp);
            height = itemView.findViewById(R.id.crimeHeight_missingComp);
            gender = itemView.findViewById(R.id.crimeGender_missingComp);
            status = itemView.findViewById(R.id.crimeStatus_missingComp);
            phone   = itemView.findViewById(R.id.crimePhone_missingComp);
            name = itemView.findViewById(R.id.personName_missingComp);
            crimeId = itemView.findViewById(R.id.crimeId_missingComp);
            crimeDate = itemView.findViewById(R.id.crimeDate_missingComp);
            location = itemView.findViewById(R.id.crimeLocation_missingComp);

        }
    }
}
