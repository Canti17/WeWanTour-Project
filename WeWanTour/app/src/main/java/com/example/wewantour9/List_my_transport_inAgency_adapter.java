package com.example.wewantour9;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.Calendar;
import java.util.List;

public class List_my_transport_inAgency_adapter extends RecyclerView.Adapter<List_my_transport_inAgency_adapter.ImageViewHolder> {
    private Context mContext;
    private List<Transport> transports;

    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference db_transport, db_agency;
    private String id_user, id_transport;

    public List_my_transport_inAgency_adapter(Context mContext, List<Transport> transports) {
        this.mContext = mContext;
        this.transports = transports;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.transport_foragency_row, parent, false);
        return new List_my_transport_inAgency_adapter.ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final Transport transport=transports.get(position);
        holder.text_destination.setText(transport.getDestination());
        holder.text_agency_name.setText(transport.getAgency());
        holder.text_start_place.setText(transport.getStartLocation());
        holder.text_start_date.setText(transport.getStartDate());
        holder.text_start_hour.setText(transport.getStartHour());
        holder.number_people.setText(Integer.toString(transport.getCurrentPeople()));
        if(transport.getVehicle().equals("Bus")){
            Glide.with(mContext)
                    .load(R.drawable.ic_directions_bus_black_24dp)
                    .into(holder.img_vehicle_transport);
        }else{
            Glide.with(mContext)
                    .load(R.drawable.car)
                    .into(holder.img_vehicle_transport);
        }

        //DELETE OF THE TRANSPORT
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        db_transport= database.getInstance().getReference("TRANSPORT");
        db_agency= database.getInstance().getReference("USER/Agency");

        //get the agency and the transport id
        db_agency.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Agency agency_buffer = postSnapshot.getValue(Agency.class);
                    if(agency_buffer.getEmail().equals(currentUser.getEmail())){
                        //get the agency id
                        id_user = postSnapshot.getKey();
                    }
                    for (DataSnapshot listTransportSnapshot : postSnapshot.child("list_transports").getChildren()) {
                        Transport buffer_transport = listTransportSnapshot.getValue(Transport.class);
                        if(buffer_transport.equals(transport)){
                            //get the transport id
                            id_transport = listTransportSnapshot.getKey();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //DELETE button onClick()
        holder.btn_delete_transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Warning")
                        .setMessage("Are you sure you want to delete this Tour?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //check if the current date is after the transport starting date
                                final Calendar c = Calendar.getInstance();
                                int current_year = c.get(Calendar.YEAR);
                                int current_month = c.get(Calendar.MONTH)+1;
                                int current_day = c.get(Calendar.DAY_OF_MONTH);

                                String[] dateSplit = transport.getStartDate().split("-");
                                Boolean transportDateBeforeCurrent = false;

                                if( (current_year>Integer.parseInt(dateSplit[2])) ||
                                        ((current_year == Integer.parseInt(dateSplit[2])) && current_month > Integer.parseInt(dateSplit[1])) ||
                                        ((current_year == Integer.parseInt(dateSplit[2])) && current_month == Integer.parseInt(dateSplit[1]) && current_day > Integer.parseInt(dateSplit[0]))){
                                    transportDateBeforeCurrent = true;
                                }
                                if(transport.getCurrentPeople()==0 || transportDateBeforeCurrent){
                                    //Delete the tour from the all TOUR list
                                    db_transport.child(id_transport).removeValue();
                                    //Delete the tour from the Agency tour list
                                    db_agency.child(id_user).child("list_transports").child(id_transport).removeValue();

                                    String text = "Transport deleted";
                                    Spannable centeredText = new SpannableString(text);
                                    centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                            0, text.length() - 1,
                                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                    Toast toast = Toast.makeText(mContext, centeredText, Toast.LENGTH_SHORT);
                                    toast.show();
                                }else{
                                    String text = "Transport cannot be cancelled because it has reservations and has not expired.";
                                    Spannable centeredText = new SpannableString(text);
                                    centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                            0, text.length() - 1,
                                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                    Toast toast = Toast.makeText(mContext, centeredText, Toast.LENGTH_LONG);
                                    toast.show();
                                }
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
    public int getItemCount() {
        return transports.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView text_destination;
        public TextView text_agency_name;
        public TextView text_start_place;
        public TextView text_start_date;
        public TextView text_start_hour;
        public TextView number_people;
        public ImageView img_vehicle_transport;
        public Button btn_delete_transport;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            text_destination=itemView.findViewById(R.id.text_destination);
            text_agency_name=itemView.findViewById(R.id.text_agency_name);
            text_start_place=itemView.findViewById(R.id.text_start_place);
            text_start_date=itemView.findViewById(R.id.text_start_date);
            text_start_hour=itemView.findViewById(R.id.text_start_hour);
            number_people=itemView.findViewById(R.id.number_people);
            img_vehicle_transport=itemView.findViewById(R.id.transport_vehicle);
            btn_delete_transport=itemView.findViewById(R.id.btn_delete_transport);

        }
    }
}
