package com.example.wewantour9;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_reservation_agency_adapter extends RecyclerView.Adapter<My_reservation_agency_adapter.ImageViewHolder> {

    private Context mContext;
    private List<Reservation> reservations;
    private Double tour_cost=0.0;
    private Double transport_cost=0.0;
    private String transport_start_place="";
    private String transport_start_hour="";
    private String transport_vhc="";
    private Integer transport_current_people=0;

    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference db_reservation, db_transport, db_tour, db_agency;

    private String id_reservation;
    private String id_reservation_tour_agency;
    private String id_reservation_tour;
    private String id_reservation_transport_agency;
    private String id_reservation_transport;
    private String id_user;

    private int newCurrentPeoplesTransport, newCurrentPeoplesTour;

    //weather
    private String buffer="";
    private JSONObject weatherData;
    private String sunrise="", sunset="", weatherId="", weatherDescritpion="", temp="", humidity="", windSpeed="";
    private Boolean weatherGoesWrong = false;
    private int orientation;

    public My_reservation_agency_adapter(Context mContext, List<Reservation> reservations) {
        this.mContext = mContext;
        this.reservations=reservations;
    }

    @NonNull
    @Override
    public My_reservation_agency_adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_reservation_row, parent, false);
        return new My_reservation_agency_adapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final My_reservation_agency_adapter.ImageViewHolder holder, int position) {
        final Reservation reservation=reservations.get(position);
        holder.text_tour_name.setText(reservation.getTour().getName());
        Glide.with(mContext)
                .load(reservation.getTour().getFilePath())
                .into(holder.img_tour);
        holder.text_tour_place.setText(reservation.getTour().getStartPlace());
        tour_cost=reservation.getTour().getPrice();
        if(reservation.getTransport()!= null){

            transport_cost=reservation.getTransport().getCost();
            transport_start_place=reservation.getTransport().getStartLocation();
            transport_start_hour=reservation.getTransport().getStartHour();
            transport_vhc=reservation.getTransport().getVehicle();
            transport_current_people=reservation.getTransportNumberOfPeople();

        }else{
            holder.itemView.findViewById(R.id.textView4).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.transport_info).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.transport_vhc).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.text_start_place).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.text_start_hour).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.imageView10).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.imageView12).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.imageView30).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.transportCurrPeople).setVisibility(View.GONE);
        }
        holder.text_total_cost.setText("€"+Double.toString((tour_cost+transport_cost)*reservation.getNumberOfPeople()));
        holder.text_transport_start_place.setText(transport_start_place);
        holder.text_transport_start_hour.setText(transport_start_hour);
        holder.text_tour_date.setText(reservation.getTour().getStartDate());
        holder.text_tour_hour.setText(reservation.getTour().getStartHour());
        holder.text_total_people.setText(String.valueOf(reservation.getNumberOfPeople()));
        holder.text_transport_current_people.setText(String.valueOf(transport_current_people));

        if(reservation.getTour().getVehicle().equals("bike")){
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_bike_black_24dp)
                    .into(holder.img_tour_vehicle);
        }else{
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_walk_black_24dp)
                    .into(holder.img_tour_vehicle);
        }

        if(transport_vhc.equals("Bus")){
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_bus_black_24dp)
                    .into(holder.img_transport_vehicle);
        }else{
            Glide.with(mContext)
                    .load(R.drawable.car)
                    .into(holder.img_transport_vehicle);
        }

        orientation = mContext.getResources().getConfiguration().orientation;

        holder.my_reservation_weather_icon.setVisibility(View.INVISIBLE);
        holder.my_reservation_temperature_icon.setVisibility(View.INVISIBLE);
        holder.my_reservation_wind_icon.setVisibility(View.INVISIBLE);
        holder.my_reservation_temperature_field.setVisibility(View.INVISIBLE);
        holder.my_reservation_wind_field.setVisibility(View.INVISIBLE);
        holder.my_reservation_weather_field.setVisibility(View.INVISIBLE);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            holder.my_reservation_humidity_icon.setVisibility(View.INVISIBLE);
            holder.my_reservation_humidity_field.setVisibility(View.INVISIBLE);
        }


        //Weather display (the buffer variable is just for the debugging)
        Thread t = new Thread(){
            public void run(){

                ArrayList<String> arrayCoordinates = API_usage.getCoordinates(mContext, reservation.getTour().getStartPlace());

                if(arrayCoordinates.get(0) != null){

                    weatherData = API_usage.getWeather(mContext, arrayCoordinates, reservation.getTour().getStartDate());

                    buffer = arrayCoordinates.toString();

                    if(weatherData != null){

                        buffer = weatherData.toString();

                        try{
                            sunrise = weatherData.getString("sunrise");
                            sunset = weatherData.getString("sunset");
                            weatherId = weatherData.getJSONArray("weather").getJSONObject(0).getString("id");
                            weatherDescritpion = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
                            weatherDescritpion = weatherDescritpion.substring(0, 1).toUpperCase() + weatherDescritpion.substring(1);
                            temp = weatherData.getJSONObject("temp").getString("day");
                            humidity = weatherData.getString("humidity");
                            windSpeed = weatherData.getString("wind_speed");
                            buffer = sunrise+" // "+sunset+" // "+weatherId+" // "+weatherDescritpion+" // "+temp+" // "+humidity+" // "+windSpeed;

                            //something to execute in the main thread, only the main thread is able to modify the view
                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    holder.my_reservation_weather_icon.setImageDrawable(API_usage.getIconFromWeatherCode(mContext, Integer.parseInt(weatherId), sunrise, sunset, reservation.getTour().getStartHour()));
                                    holder.my_reservation_temperature_field.setText(temp + " C°");
                                    holder.my_reservation_humidity_field.setText(humidity + "%");
                                    holder.my_reservation_wind_field.setText(windSpeed + " m/s");
                                    holder.my_reservation_weather_progressbar.setVisibility(View.GONE);
                                    holder.my_reservation_weather_icon.setVisibility(View.VISIBLE);
                                    holder.my_reservation_temperature_icon.setVisibility(View.VISIBLE);
                                    holder.my_reservation_wind_icon.setVisibility(View.VISIBLE);
                                    holder.my_reservation_temperature_field.setVisibility(View.VISIBLE);
                                    holder.my_reservation_wind_field.setVisibility(View.VISIBLE);
                                    holder.my_reservation_weather_field.setVisibility(View.VISIBLE);
                                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                        holder.my_reservation_humidity_icon.setVisibility(View.VISIBLE);
                                        holder.my_reservation_humidity_field.setVisibility(View.VISIBLE);
                                    }

                                }
                            });

                        }catch (Exception e){
                            //Case the parse of the weather response not goes well
                            weatherGoesWrong = true;
                            buffer = e + weatherData.toString();
                        }
                    }else{
                        //Case api don't find the meteo
                        weatherGoesWrong = true;
                        buffer = "NULL weather ";
                    }
                }else{
                    //Case api don't find the coordinates
                    weatherGoesWrong = true;
                    buffer = "NULL coordinates";
                }

                if(weatherGoesWrong){
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.my_reservation_weather_progressbar.setVisibility(View.GONE);
                        }
                    });
                }

                Log.e("WEATHER/GEOCODING API DEBUG", buffer);

            }
        };
        t.start();

        //DELETE OF THE RESERVATION
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        db_reservation= database.getInstance().getReference("RESERVATION");
        db_transport= database.getInstance().getReference("TRANSPORT");
        db_tour= database.getInstance().getReference("TOUR");
        db_agency= database.getInstance().getReference("USER/Agency");

        //Get the reservation id
        db_reservation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reservation reservation_buffer = postSnapshot.getValue(Reservation.class);
                    if(reservation_buffer.equals(reservation)){
                        id_reservation = postSnapshot.getKey();
                        Log.e("my_reservation_agency_adapter ID OF THE RESERVATION:", id_reservation);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //get the agency id of the reservation tour and transport and of the respective agencies + evaluate the new number of reservations in case of delete
        db_agency.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Agency agency_buffer = postSnapshot.getValue(Agency.class);
                    if(agency_buffer.getEmail().equals(reservation.getTour().getAgency())){
                        //get the reservation tour agency id
                        id_reservation_tour_agency = postSnapshot.getKey();
                        Log.e("my_reservation_agency_adapter ID OF THE AGENCY OF THE TOUR OF THE RESERVATION:", id_reservation_tour_agency);
                        for (DataSnapshot listTourSnapshot : postSnapshot.child("list_tour").getChildren()) {
                            Tour buffer_tour = listTourSnapshot.getValue(Tour.class);
                            if(buffer_tour.equals(reservation.getTour())){
                                //get the reservation tour id
                                id_reservation_tour = listTourSnapshot.getKey();
                                Log.e("my_reservation_agency_adapter ID OF THE RESERVED TOUR:", id_reservation_tour);
                                //get the new number of tour reservations in the case of deletion
                                newCurrentPeoplesTour = buffer_tour.getCurrentPeople() - reservation.getNumberOfPeople();
                            }
                        }
                    }
                    if(reservation.getTransport() != null) {
                        if (agency_buffer.getEmail().equals(reservation.getTransport().getAgency())) {
                            //get the reservation transport agency id
                            id_reservation_transport_agency = postSnapshot.getKey();
                            Log.e("my_reservation_agency_adapter ID OF THE AGENCY OF THE TRANSPORT OF THE RESERVATION:", id_reservation_transport_agency);
                            for (DataSnapshot listTransportSnapshot : postSnapshot.child("list_transports").getChildren()) {
                                Transport buffer_transport = listTransportSnapshot.getValue(Transport.class);
                                if (buffer_transport.equals(reservation.getTransport())) {
                                    //get the reservation transport id
                                    id_reservation_transport = listTransportSnapshot.getKey();
                                    Log.e("my_reservation_agency_adapter ID OF THE RESERVED TRANSPORT:", id_reservation_tour);
                                    //get the new number of transport reservations in the case of deletion
                                    newCurrentPeoplesTransport = buffer_transport.getCurrentPeople() - reservation.getTransportNumberOfPeople();
                                }
                            }
                        }
                    }
                    if(agency_buffer.getEmail().equals(currentUser.getEmail())){
                        //get the user id
                        id_user = postSnapshot.getKey();
                        Log.e("my_reservation_agency_adapter ID OF THE CURRENT USER (AGENCY):", id_reservation_tour);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        holder.btn_delete_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Warning")
                        .setMessage("Are you sure you want to delete this Reservation?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //Delete the reservation from the general RESERVATIONS list
                                db_reservation.child(id_reservation).removeValue();

                                //Delete the reservation from the list of reservation of the customer
                                db_agency.child(id_user).child("list_reservation").child(id_reservation).removeValue();

                                Map<String, Object> updateMap = new HashMap<>();

                                if( (reservation.getTransport() != null) && (id_reservation_transport != null)){
                                    //Update the transport in the all TRANSPORT list of the database
                                    updateMap.put("currentPeople", newCurrentPeoplesTransport);
                                    db_transport.child(id_reservation_transport).updateChildren(updateMap);

                                    //Update the transport in the Agency Transport List
                                    db_agency.child(id_reservation_transport_agency).child("list_transports").child(id_reservation_transport).updateChildren(updateMap);
                                }

                                if(id_reservation_tour != null){

                                    //Update the tour in the Agency Tour List
                                    updateMap.put("currentPeople", newCurrentPeoplesTour);
                                    db_tour.child(id_reservation_tour).updateChildren(updateMap);

                                    //Update the tour in the Agency Tour List
                                    db_agency.child(id_reservation_tour_agency).child("list_tour").child(id_reservation_tour).updateChildren(updateMap);

                                }
                                String text = "Reservation deleted";
                                Spannable centeredText = new SpannableString(text);
                                centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                        0, text.length() - 1,
                                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                Toast toast = Toast.makeText(mContext, centeredText, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        //.setIcon(getResources().getDrawable(R.drawable.error))
                        .show();
            }
        });
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
        public Button btn_delete_reservation;
        public TextView text_transport_current_people;
        public ImageView my_reservation_weather_icon;
        public ImageView my_reservation_temperature_icon;
        public ImageView my_reservation_humidity_icon;
        public ImageView my_reservation_wind_icon;
        public TextView my_reservation_temperature_field;
        public TextView my_reservation_humidity_field;
        public TextView my_reservation_wind_field;
        public TextView my_reservation_weather_field;
        public ProgressBar my_reservation_weather_progressbar;


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
            btn_delete_reservation=itemView.findViewById(R.id.btn_delete_reservation);
            text_transport_current_people= itemView.findViewById(R.id.transportCurrPeople);
            my_reservation_weather_icon = itemView.findViewById(R.id.imageViewMyReservationRowWeather);
            my_reservation_temperature_icon = itemView.findViewById(R.id.imageViewMyReservationRowTemperature);
            my_reservation_humidity_icon = itemView.findViewById(R.id.imageViewMyReservationRowHumidity);
            my_reservation_wind_icon = itemView.findViewById(R.id.imageViewMyReservationRowWind);
            my_reservation_temperature_field = itemView.findViewById(R.id.textViewMyReservationRowTemperature);
            my_reservation_humidity_field = itemView.findViewById(R.id.textViewMyReservationRowHumidity);
            my_reservation_wind_field = itemView.findViewById(R.id.textViewMyReservationsRowWind);
            my_reservation_weather_field = itemView.findViewById(R.id.textViewMyReservationRowWeather);
            my_reservation_weather_progressbar = itemView.findViewById(R.id.progressBarMyReservationWeather);

        }
    }
}
