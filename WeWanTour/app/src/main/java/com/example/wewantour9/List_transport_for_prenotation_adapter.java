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

import java.util.LinkedList;
import java.util.List;

public class List_transport_for_prenotation_adapter extends RecyclerView.Adapter<List_transport_for_prenotation_adapter.ImageViewHolder> {
    private Context mContext;
    private List<Transport> transports;
    private Transport transport;
    private Activity activity;

    public List_transport_for_prenotation_adapter(Context mContext, List<Transport> transports, Activity activity) {
        this.mContext = mContext;
        this.transports = transports;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.transport_for_customer_row, parent, false);
        return new List_transport_for_prenotation_adapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        transport=transports.get(position);
        holder.text_destination.setText(transport.getDestination());
        holder.text_agency_name.setText(transport.getAgency());
        holder.text_start_place.setText(transport.getStartLocation());
        holder.text_start_date.setText(transport.getStartDate());
        holder.text_start_hour.setText(transport.getStartHour());
        if(transport.getVehicle().equals("Bus")){
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_bus_black_24dp)
                    .into(holder.img_vehicle_transport);
        }else{
            Glide.with(mContext)
                    .load(R.drawable.car)
                    .into(holder.img_vehicle_transport);
        }

        holder.select_transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Transport from list Transports for booking", transport);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() { return transports.size(); }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView text_destination;
        public TextView text_agency_name;
        public TextView text_start_place;
        public TextView text_start_date;
        public TextView text_start_hour;
        public ImageView img_vehicle_transport;
        public Button select_transport;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            text_destination=itemView.findViewById(R.id.text_destination);
            text_agency_name=itemView.findViewById(R.id.text_agency_name);
            text_start_place=itemView.findViewById(R.id.text_start_place);
            text_start_date=itemView.findViewById(R.id.text_start_date);
            text_start_hour=itemView.findViewById(R.id.text_start_hour);
            img_vehicle_transport=itemView.findViewById(R.id.transport_vehicle);
            select_transport=itemView.findViewById(R.id.select_transport);

        }
    }
}
