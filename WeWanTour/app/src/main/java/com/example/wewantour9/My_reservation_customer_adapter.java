package com.example.wewantour9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class My_reservation_customer_adapter extends RecyclerView.Adapter<My_reservation_customer_adapter.ImageViewHolder> {

    private Context mContext;
    private List<Reservation> reservations;

    public My_reservation_customer_adapter(Context mContext, List<Reservation> reservations) {
        this.mContext = mContext;
        this.reservations=reservations;
    }

    @NonNull
    @Override
    public My_reservation_customer_adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_reservation_customer_row, parent, false);
        return new My_reservation_customer_adapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull My_reservation_customer_adapter.ImageViewHolder holder, int position) {
        final Reservation reservation=reservations.get(position);
        holder.text_tour_name.setText(reservation.getTour().getName());
        Glide.with(mContext)
                .load(reservation.getTour().getFilePath())
                .into(holder.img_tour);
        holder.text_tour_place.setText(reservation.getTour().getStartPlace());
        holder.text_total_cost.setText("€"+Double.toString(Double.valueOf(reservation.getTour().getPrice())+Double.valueOf(reservation.getTransport().getCost())));
        holder.text_transport_start_place.setText(reservation.getTransport().getStartLocation());
        holder.text_transport_start_hour.setText(reservation.getTransport().getStartHour());
        holder.text_tour_date.setText(reservation.getTour().getStartDate());
        holder.text_tour_hour.setText(reservation.getTour().getStartHour());
        holder.text_total_people.setText(String.valueOf(reservation.getNumberOfPeople()));

        if(reservation.getTour().getVehicle().equals("bike")){
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_bike_black_24dp)
                    .into(holder.img_tour_vehicle);
        }else{
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_walk_black_24dp)
                    .into(holder.img_tour_vehicle);
        }

        if(reservation.getTransport().getVehicle().equals("Bus")){
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_bus_black_24dp)
                    .into(holder.img_transport_vehicle);
        }else{
            Glide.with(mContext)
                    .load(R.drawable.car)
                    .into(holder.img_transport_vehicle);
        }
    }


    @Override
    public int getItemCount() { return reservations.size(); }


    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView text_tour_name;
        public ImageView img_tour;
        public ImageView img_tour_vehicle;
        public ImageView img_transport_vehicle;
        public TextView text_tour_date;
        public TextView text_tour_hour;
        public TextView text_tour_place;
        public TextView text_total_cost;
        public TextView text_total_people;
        public TextView text_transport_start_place;
        public TextView text_transport_start_hour;



        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            text_tour_name=itemView.findViewById(R.id.tour_name);
            img_tour=itemView.findViewById(R.id.img_tour);
            img_tour_vehicle=itemView.findViewById(R.id.tour_vhc);
            img_transport_vehicle=itemView.findViewById(R.id.transport_vhc);
            text_tour_date=itemView.findViewById(R.id.tour_date);
            text_tour_hour=itemView.findViewById(R.id.tour_hour);
            text_tour_place=itemView.findViewById(R.id.text_destination);
            text_total_people=itemView.findViewById(R.id.num_people);
            text_total_cost=itemView.findViewById(R.id.text_cost);
            text_transport_start_place=itemView.findViewById(R.id.text_start_place);
            text_transport_start_hour=itemView.findViewById(R.id.text_start_hour);
        }
    }
}
