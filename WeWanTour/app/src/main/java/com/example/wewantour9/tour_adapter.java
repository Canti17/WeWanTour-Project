package com.example.wewantour9;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;



public class tour_adapter extends RecyclerView.Adapter<tour_adapter.ImageViewHolder>{
    private Context mContext;
    private List<Tour> uploads;


    private String testo;


    public tour_adapter(Context mContext, List<Tour> uploads) {
        this.mContext = mContext;
        this.uploads = uploads;
    }




    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.tour_row, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        final Tour current_tour=uploads.get(position);
        testo = current_tour.getName();
        holder.text_name.setText(current_tour.getName());
        holder.text_price.setText("€ "+Double.toString(current_tour.getPrice()));
        holder.text_date.setText(current_tour.getStartDate());
        holder.text_hour.setText(current_tour.getStartHour());

        if(current_tour.getVehicle().equals("bike")){
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_bike_black_24dp)
                    .into(holder.vehicle_img);
        }else{
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_walk_black_24dp)
                    .into(holder.vehicle_img);
        }

        Glide.with(mContext)
                .load(current_tour.getFilePath())
                .into(holder.back_img);



        /* PER ANDREA, questo gestisce il click sull'item del RecyclerView, una volta cliccato hai il riferimento al tour per poterlo
            andare a cercare nel db il tour e rappresentarlo nella pagina. Mi so rotto di scrive, fai tu, BA, FINE.*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, tour_details.class);
                intent.putExtra("Tour class from HomePage", uploads.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }
    


    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView text_name;
        public TextView text_price;
        public TextView text_date;
        public TextView text_hour;
        public ImageView back_img;
        public ImageView vehicle_img;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            text_name=itemView.findViewById(R.id.text_name);
            text_price=itemView.findViewById(R.id.text_price);
            text_date=itemView.findViewById(R.id.text_date);
            text_hour=itemView.findViewById(R.id.text_hour);
            back_img=itemView.findViewById(R.id.back_img);
            vehicle_img=itemView.findViewById(R.id.vehicle_img);
        }


        public String getText_name() {
            return text_name.toString();
        }

        public TextView getText_price() {
            return text_price;
        }

        public TextView getText_date() {
            return text_date;
        }

        public TextView getText_hour() {
            return text_hour;
        }

        public ImageView getVehicle_img() {
            return vehicle_img;
        }
    }


    public String getTesto() {
        return testo;
    }
    
    
    
}
