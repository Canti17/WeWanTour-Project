package com.example.wewantour9;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class List_tour_inAgency_adapter extends RecyclerView.Adapter<List_tour_inAgency_adapter.ImageViewHolder> {

    private Context mContext;
    private List<Tour> uploads;

    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference db_transport, db_tour, db_agency;
    private String id_user;
    private ArrayList<String> id_tour;
    private ArrayList<ArrayList<String>> id_transports_agencies = new ArrayList<ArrayList<String>>();
    private int numberOfToursWithTheSameDestination = 0;
    private FirebaseStorage storage;
    private StorageReference deleteStorageImageReference;
    private String url ;
    private List<String> jsonResponses = new ArrayList<>();

    public List_tour_inAgency_adapter(Context mContext, List<Tour> uploads) {
        this.mContext = mContext;
        this.uploads = uploads;
        this.id_tour = new ArrayList<String>(Collections.nCopies(uploads.size(), ""));
        url = mContext.getResources().getString(R.string.URLServer);
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

        //DELETE OF THE RESERVATION
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        db_transport= database.getInstance().getReference("TRANSPORT");
        db_tour= database.getInstance().getReference("TOUR");
        db_agency= database.getInstance().getReference("USER/Agency");
        deleteStorageImageReference = storage.getInstance().getReferenceFromUrl(current_tour.getFilePath());

        //get all the transports with the respective agencies list with List (agencyEmail, id_transport, _)
        db_transport.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Transport transport_buffer = postSnapshot.getValue(Transport.class);
                    if(transport_buffer.getDestination().equals(current_tour.getStartPlace())){
                        ArrayList<String> buffer_id = new ArrayList<String>();
                        buffer_id.add(transport_buffer.getAgency());
                        buffer_id.add(postSnapshot.getKey());
                        id_transports_agencies.add(buffer_id);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //count the number of tours with the same starting place of the current tour
        db_tour.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Tour tour_buffer = postSnapshot.getValue(Tour.class);
                    if(tour_buffer.getStartPlace().equals(current_tour.getStartPlace())){
                        numberOfToursWithTheSameDestination ++;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //get the agency and the tour id
        db_agency.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Agency agency_buffer = postSnapshot.getValue(Agency.class);
                    if(agency_buffer.getEmail().equals(currentUser.getEmail())){
                        //get the agency id
                        id_user = postSnapshot.getKey();
                    }
                    for (DataSnapshot listTourSnapshot : postSnapshot.child("list_tour").getChildren()) {
                        Tour buffer_tour = listTourSnapshot.getValue(Tour.class);
                        if(buffer_tour.equals(current_tour)){
                            //get the tour id
                            id_tour.set(position, listTourSnapshot.getKey());
                        }
                    }
                    //get the agency id of all the transports related to that tour List (agencyEmail, id_transport, id_agency)
                    for(ArrayList<String> ls : id_transports_agencies){
                        if(ls.get(0).equals(agency_buffer.getEmail())){
                            ls.add(postSnapshot.getKey());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });





        //DELETE button onClick()
        holder.btn_delete_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext)
                        .setTitle("Warning")
                        .setMessage("Are you sure you want to delete this Tour?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                //DELETE CALL TO TOUR NAME
                                RequestQueue requestQueue = Volley.newRequestQueue(mContext);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url+"nometour="+uploads.get(position).getName(), null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            boolean msg = response.getBoolean("error");

                                            Log.i("VOLLEY", "Ha funzionato");
                                            Log.i("VOLLEY", "La risposta è:"+msg);


                                            if(!msg){
                                                //check if the current date is after the tour starting date
                                                final Calendar c = Calendar.getInstance();
                                                int current_year = c.get(Calendar.YEAR);
                                                int current_month = c.get(Calendar.MONTH)+1;
                                                int current_day = c.get(Calendar.DAY_OF_MONTH);

                                                String[] dateSplit = current_tour.getStartDate().split("/");
                                                Boolean tourDateBeforeCurrent = false;

                                                if( (current_year>Integer.parseInt(dateSplit[2])) ||
                                                        ((current_year == Integer.parseInt(dateSplit[2])) && current_month > Integer.parseInt(dateSplit[1])) ||
                                                        ((current_year == Integer.parseInt(dateSplit[2])) && current_month == Integer.parseInt(dateSplit[1]) && current_day > Integer.parseInt(dateSplit[0]))){
                                                    tourDateBeforeCurrent = true;
                                                }

                                                if(current_tour.getCurrentPeople()==0 || tourDateBeforeCurrent){
                                                    //Delete the tour from the all TOUR list
                                                    db_tour.child(id_tour.get(position)).removeValue();
                                                    //Delete the tour from the Agency tour list
                                                    db_agency.child(id_user).child("list_tour").child(id_tour.get(position)).removeValue();

                                                    Uri temp_path=Uri.parse("https://firebasestorage.googleapis.com/v0/b/wewantour9-f9fcd.appspot.com/o/defaultImageForTour.png?alt=media&token=d28b03a3-7e7c-49ab-a8f6-0abf734bb80c");
                                                    if(!current_tour.getFilePath().equals(temp_path.toString())){
                                                        //Delete the tour image from the firebase storage
                                                        deleteStorageImageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                String text = "Tour deleted";
                                                                Spannable centeredText = new SpannableString(text);
                                                                centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                                                        0, text.length() - 1,
                                                                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                                                Toast toast = Toast.makeText(mContext, centeredText, Toast.LENGTH_SHORT);
                                                                toast.show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                String text = "Error in the tour deletion, try again";
                                                                Spannable centeredText = new SpannableString(text);
                                                                centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                                                        0, text.length() - 1,
                                                                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                                                Toast toast = Toast.makeText(mContext, centeredText, Toast.LENGTH_LONG);
                                                                toast.show();
                                                            }
                                                        });
                                                    }



                                                    if(numberOfToursWithTheSameDestination < 2){
                                                        for(ArrayList<String> ls : id_transports_agencies){
                                                            //Delete all the transports that have as destination this tour from all TRANSPORT list
                                                            db_transport.child(ls.get(1)).removeValue();
                                                            //Delete all the transports that have as destination this tour from the respective agency transport list
                                                            db_agency.child(ls.get(2)).child("list_transports").child(ls.get(1)).removeValue();
                                                        }
                                                    }
                                                }
                                                else{
                                                    String text = "Tour cannot be cancelled because it has reservations and has not expired.";
                                                    Spannable centeredText = new SpannableString(text);
                                                    centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                                            0, text.length() - 1,
                                                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                                    Toast toast = Toast.makeText(mContext, centeredText, Toast.LENGTH_LONG);
                                                    toast.show();
                                                }

                                            }

                                            jsonResponses.add(String.valueOf(msg));
                                            // }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        Log.e("VOLLEY","Errore");
                                        Log.e("VOLLEY",error.toString());
                                        String text = "Error in the tour deletion, try again";
                                        Spannable centeredText = new SpannableString(text);
                                        centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                                0, text.length() - 1,
                                                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                        Toast toast = Toast.makeText(mContext, centeredText, Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });

                                requestQueue.add(jsonObjectRequest);








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
