package com.example.wewantour9;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

public class List_tour_inAgency_adapter extends RecyclerView.Adapter<List_tour_inAgency_adapter.ImageViewHolder> {

    private Context mContext;
    private List<Tour> uploads;

    public List_tour_inAgency_adapter(Context mContext, List<Tour> uploads) {
        this.mContext = mContext;
        this.uploads = uploads;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.tour_foragency_row, parent, false);
        return new List_tour_inAgency_adapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        final Tour current_tour=uploads.get(position);
        holder.text_name_tour.setText(current_tour.getName());

        holder.text_start_place.setText(current_tour.getStartPlace());
        holder.text_date_tour.setText(current_tour.getStartDate());
        holder.text_hour_tour.setText(current_tour.getStartHour());
        
        if(current_tour.getVehicle().equals("bike")){
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_bike_black_24dp)
                    .into(holder.img_vehicle_tour);
        }else{
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_walk_black_24dp)
                    .into(holder.img_vehicle_tour);
        }

        Glide.with(mContext)
                .load(current_tour.getFilePath())
                .into(holder.img_tour);

        //Passaggio ad add_tour con l'ogetto PER RICCARDO se l'idea è mettere il bottone sposta il codice nell'onClick del bottone KISS


        holder.img_edit_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, edit_tour.class);
                intent.putExtra("Tour class from ListTourInAgency", uploads.get(position));
                intent.putExtra("Tour list from ListTourInAgency", (Serializable) uploads);
                mContext.startActivity(intent);
            }
        });


        holder.btn_delete_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new for ANDREA
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView text_name_tour;
        public TextView text_name_agency;
        public TextView text_start_place;
        public TextView text_date_tour;
        public TextView text_hour_tour;
        public ImageView img_vehicle_tour;
        public ImageView img_tour;
        public Button btn_add_transport;
        public ImageView img_edit_tour;
        public Button btn_delete_tour;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            text_name_tour =itemView.findViewById(R.id.text_name_tour);
            text_start_place=itemView.findViewById(R.id.text_start_place);
            text_date_tour =itemView.findViewById(R.id.text_date_tour);
            text_hour_tour =itemView.findViewById(R.id.text_hour_tour);
            img_vehicle_tour= itemView.findViewById(R.id.img_vehicle_tour);
            img_tour= itemView.findViewById(R.id.img_tour);
            btn_add_transport=itemView.findViewById(R.id.btn_add_transport);
            img_edit_tour=itemView.findViewById(R.id.btn_edit_tour);
            btn_delete_tour=itemView.findViewById(R.id.btn_delete_tour);
        }
    }
}
