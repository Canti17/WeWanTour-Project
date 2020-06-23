package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class reservation_summary extends AppCompatActivity {

    private TextView summary;
    private Button submitButton;
    private Reservation reservation;
    private int newCurrentPeoplesTransport, newCurrentPeoplesTour;

    private String new_reservation_id;
    private FirebaseDatabase database;
    private DatabaseReference db_reservation, db_customer_reservations, db_transport, db_tour, db_agency;


    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;

    public String getNextId(DataSnapshot postSnapshot) {
        ArrayList<String> lastList = new ArrayList<String>();
        for (DataSnapshot postSnapshotList : postSnapshot.getChildren()) {
            lastList.add(postSnapshotList.getKey());
        }
        int id_progressive;
        if(lastList.size() != 0) {
            id_progressive = Integer.parseInt(lastList.get(lastList.size() - 1)) + 1;
        }else{
            id_progressive = 0;
        }
        return String.valueOf(id_progressive);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_summary);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        summary = findViewById(R.id.textViewSummary);
        submitButton = findViewById(R.id.buttonSummaryConfirmBooking);

        db_reservation= database.getInstance().getReference("RESERVATION");
        db_customer_reservations= database.getInstance().getReference("USER/Customer");
        db_transport= database.getInstance().getReference("TRANSPORT");
        db_tour= database.getInstance().getReference("TOUR");
        db_agency= database.getInstance().getReference("USER/Agency");

        //Id of each reservation
        db_reservation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> lastList = new ArrayList<String>();
                for (DataSnapshot postSnapshotList : dataSnapshot.getChildren()) {
                    lastList.add(postSnapshotList.getKey());
                }
                int id_progressive;
                if(lastList.size() != 0) {
                    id_progressive = Integer.parseInt(lastList.get(lastList.size() - 1)) + 1;
                }else{
                    id_progressive = 0;
                }
                new_reservation_id =  String.valueOf(id_progressive);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        reservation =  (Reservation) getIntent().getSerializableExtra("Reservation class from tour details to summary");

        summary.setText(reservation.toString());


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db_customer_reservations.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        db_reservation.child(String.valueOf(new_reservation_id)).setValue(reservation);
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Customer customer_crnt = postSnapshot.getValue(Customer.class);
                            if(customer_crnt.getEmail().equals(currentUser.getEmail())) {
                                db_customer_reservations.child(postSnapshot.getKey()).child("list_reservation").child(getNextId(postSnapshot.child("list_reservation"))).setValue(reservation);
                                //db_customer_reservations.child(postSnapshot.getKey()).child("list_reservation").child(new_reservation_id).setValue(reservation); //QUESTA RIGA VA SOSTITUITA ALLA PRECEDENTE QUANDO DECIDIAMO DI NON CANCELLARE PIU COSE A CAVOLO, SERVE AD AVERE UNA CONGRUENZA NEL DB TRA GLI ID /RESERVATION & /USER/Customer/list_reservation WHEN THIS LINE USED DELETE THE FUNCTION "getNextId" ABOVE
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                //
                //from here all should be changed and more simple when the tours and transports id coerence will be inserted (no more random delete and all the second line commented used for the insertion)
                //
                if(reservation.getTransport()!=null) {
                    //Update the transport in the all TRASPORT list of the database
                    db_transport.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Transport buffer_transport = postSnapshot.getValue(Transport.class);
                                if (buffer_transport.equals(reservation.getTransport())) {
                                    Map<String, Object> updateMap = new HashMap<>();
                                    newCurrentPeoplesTransport = buffer_transport.getCurrentPeople()+reservation.getNumberOfPeople();
                                    updateMap.put("currentPeople", newCurrentPeoplesTransport);
                                    db_transport.child(postSnapshot.getKey()).updateChildren(updateMap);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    //Update the transport in the Agency Transport List
                    db_agency.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Agency buffer_agency = postSnapshot.getValue(Agency.class);
                                if (buffer_agency.getEmail().equals(reservation.getTransport().getAgency())) {
                                    for (DataSnapshot listTourSnapshot : postSnapshot.child("list_transports").getChildren()) {
                                        Transport agency_transport = listTourSnapshot.getValue(Transport.class);
                                        if(agency_transport.equals(reservation.getTransport())){
                                            Map<String, Object> updateMap = new HashMap<>();
                                            updateMap.put("currentPeople", newCurrentPeoplesTransport);
                                            db_agency.child(postSnapshot.getKey()).child("list_transports").child(listTourSnapshot.getKey()).updateChildren(updateMap);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                //Update the tour in the all TOUR list of the database
                db_tour.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Tour buffer_tour = postSnapshot.getValue(Tour.class);
                            if (buffer_tour.equals(reservation.getTour())) {
                                Map<String, Object> updateMap = new HashMap<>();
                                newCurrentPeoplesTour = buffer_tour.getCurrentPeople() + reservation.getNumberOfPeople();
                                updateMap.put("currentPeople", newCurrentPeoplesTour);
                                db_tour.child(postSnapshot.getKey()).updateChildren(updateMap);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                //Update the tour in the Agency Tour List
                db_agency.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Agency buffer_agency = postSnapshot.getValue(Agency.class);
                            if (buffer_agency.getEmail().equals(reservation.getTour().getAgency())) {
                                for (DataSnapshot listTourSnapshot : postSnapshot.child("list_tour").getChildren()) {
                                    Tour agency_tour = listTourSnapshot.getValue(Tour.class);
                                    if(agency_tour.equals(reservation.getTour())){
                                        Map<String, Object> updateMap = new HashMap<>();
                                        updateMap.put("currentPeople", newCurrentPeoplesTour);
                                        db_agency.child(postSnapshot.getKey()).child("list_tour").child(listTourSnapshot.getKey()).updateChildren(updateMap);
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

    }
}