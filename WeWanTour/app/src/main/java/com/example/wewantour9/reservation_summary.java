package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Objects;

public class reservation_summary extends AppCompatActivity {

    private TextView tourTitleValue, tourDateValue, tourHourValue, tourPlaceValue, transportDateValue, transportHourValue, transportPlaceValue, priceNumberPeople, priceNumberPeople1, priceTourCost, priceTransportCost, priceTotalCost, priceTransportLabel, priceTransportX, priceTransportPlus, priceTourPlus;
    private Button submitButton;
    private ImageView tourVehicleValue, transportVehicleValue;
    private ConstraintLayout transportLayout;
    private Reservation reservation;
    private int newCurrentPeoplesTransport, newCurrentPeoplesTour;
    private Boolean currentUserIsCustomer = false;

    private Toolbar toolbar;
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

        tourTitleValue = findViewById(R.id.textViewReservationSummary);
        tourDateValue = findViewById(R.id.textViewReservationSummaryTourDateValue);
        tourHourValue = findViewById(R.id.textViewReservationSummaryTourHourValue);
        tourPlaceValue = findViewById(R.id.textViewReservationSummaryTourPlaceValue);
        tourVehicleValue = findViewById(R.id.imageViewReservationSummaryTourVehicleValue);

        transportDateValue = findViewById(R.id.textViewReservationSummaryTransportDateValue);
        transportHourValue = findViewById(R.id.textViewReservationSummaryTransportHourValue);
        transportPlaceValue = findViewById(R.id.textViewReservationSummaryTransportPlaceValue);
        transportVehicleValue = findViewById(R.id.imageViewReservationSummaryTransportVehicleValue);
        transportLayout = findViewById(R.id.ConstraintLayoutReservationSummaryTransport);

        priceTourCost = findViewById(R.id.textViewReservationSummaryPriceTourValue);
        priceTransportCost = findViewById(R.id.textViewReservationSummaryPriceTransportValue);
        priceNumberPeople = findViewById(R.id.textViewReservationSummaryPricePeopleValue);
        priceNumberPeople1 = findViewById(R.id.textViewReservationSummaryPricePeopleValue1);
        priceTotalCost = findViewById(R.id.textViewReservationSummaryPriceTotValue);

        priceTransportLabel = findViewById(R.id.textViewReservationSummaryPriceTransportLabel);
        priceTransportX = findViewById(R.id.textViewReservationSummaryPriceXLabel1);
        priceTransportPlus = findViewById(R.id.textViewReservationSummaryPricePlusLabel1);
        priceTourPlus = findViewById(R.id.textViewReservationSummaryPricePlusLabel);

        submitButton = findViewById(R.id.buttonSummaryConfirmBooking);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



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
        double totalCost;

        //set tour field
        tourTitleValue.setText(reservation.getTour().getName());
        tourDateValue.setText(reservation.getTour().getStartDate());
        tourHourValue.setText(reservation.getTour().getStartHour());
        tourPlaceValue.setText(reservation.getTour().getStartPlace());
        Drawable myDrawable;
        if(reservation.getTour().getVehicle().equals("bike"))
            myDrawable = getResources().getDrawable(R.drawable.ic_directions_bike_black_24dp);
        else
            myDrawable = getResources().getDrawable(R.drawable.ic_directions_walk_black_24dp);
        tourVehicleValue.setImageDrawable(myDrawable);
        double tourCost = reservation.getTour().getPrice();
        if((tourCost-(int)tourCost)!=0)
            priceTourCost.setText(String.valueOf(tourCost) + " €");
        else
            priceTourCost.setText(String.valueOf((int)tourCost)+ " €");
        priceNumberPeople.setText(String.valueOf(reservation.getNumberOfPeople()));
        //set transport field
        if(reservation.getTransport()==null){
            transportLayout.setVisibility(View.GONE);
            priceTourPlus.setVisibility(View.GONE);
            priceTransportLabel.setVisibility(View.GONE);
            priceTransportX.setVisibility(View.GONE);
            priceTransportPlus.setVisibility(View.GONE);
            priceTransportCost.setVisibility(View.GONE);
            priceNumberPeople1.setVisibility(View.GONE);
            totalCost = tourCost*reservation.getNumberOfPeople();
        }else{
            transportDateValue.setText(reservation.getTransport().getStartDate());
            transportHourValue.setText(reservation.getTransport().getStartHour());
            transportPlaceValue.setText(reservation.getTransport().getStartLocation());
            Drawable myDrawable1;
            if(reservation.getTransport().getVehicle().equals("Bus"))
                myDrawable1 = getResources().getDrawable(R.drawable.bus);
            else
                myDrawable1 = getResources().getDrawable(R.drawable.car);
            transportVehicleValue.setImageDrawable(myDrawable1);
            double transportCost = reservation.getTransport().getCost();
            if((transportCost-(int)transportCost)!=0)
                priceTransportCost.setText(String.valueOf(transportCost) + " €");
            else
                priceTransportCost.setText(String.valueOf((int)transportCost)+ " €");
            priceNumberPeople1.setText(String.valueOf(reservation.getNumberOfPeople()));
            totalCost = (tourCost+transportCost)*reservation.getNumberOfPeople();
        }

        if((totalCost-(int)totalCost)!=0)
            priceTotalCost.setText(String.valueOf(totalCost)+ " €");
        else
            priceTotalCost.setText(String.valueOf((int)totalCost)+ " €");













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
                                currentUserIsCustomer = true;
                                db_customer_reservations.child(postSnapshot.getKey()).child("list_reservation").child(getNextId(postSnapshot.child("list_reservation"))).setValue(reservation);
                                //db_customer_reservations.child(postSnapshot.getKey()).child("list_reservation").child(new_reservation_id).setValue(reservation); //QUESTA RIGA VA SOSTITUITA ALLA PRECEDENTE QUANDO DECIDIAMO DI NON CANCELLARE PIU COSE A CAVOLO, SERVE AD AVERE UNA CONGRUENZA NEL DB TRA GLI ID /RESERVATION & /USER/Customer/list_reservation WHEN THIS LINE USED DELETE THE FUNCTION "getNextId" ABOVE
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                if(!currentUserIsCustomer){
                    db_agency.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Agency agency_crnt = postSnapshot.getValue(Agency.class);
                                if(agency_crnt.getEmail().equals(currentUser.getEmail())) {
                                    db_agency.child(postSnapshot.getKey()).child("list_reservation").child(getNextId(postSnapshot.child("list_reservation"))).setValue(reservation);
                                    //db_agency.child(postSnapshot.getKey()).child("list_reservation").child(new_reservation_id).setValue(reservation); //QUESTA RIGA VA SOSTITUITA ALLA PRECEDENTE QUANDO DECIDIAMO DI NON CANCELLARE PIU COSE A CAVOLO, SERVE AD AVERE UNA CONGRUENZA NEL DB TRA GLI ID /RESERVATION & /USER/Customer/list_reservation WHEN THIS LINE USED DELETE THE FUNCTION "getNextId" ABOVE
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

                //
                //from here all should be changed and more simple when the tours and transports id coerence will be inserted (no more random delete and all the second line commented used for the insertion)
                //
                //Update the transport in the all TRASPORT list of the database
                if(reservation.getTransport()!=null) {
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

                db_agency.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Agency buffer_agency = postSnapshot.getValue(Agency.class);

                            //Update the tour in the Agency Tour List
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

                            //Update the transport in the Agency Transport List
                            if(reservation.getTransport()!=null) {
                                if (buffer_agency.getEmail().equals(reservation.getTransport().getAgency())) {
                                    for (DataSnapshot listTransportSnapshot : postSnapshot.child("list_transports").getChildren()) {
                                        Transport agency_transport = listTransportSnapshot.getValue(Transport.class);
                                        if(agency_transport.equals(reservation.getTransport())){
                                            Map<String, Object> updateMap = new HashMap<>();
                                            updateMap.put("currentPeople", newCurrentPeoplesTransport);
                                            db_agency.child(postSnapshot.getKey()).child("list_transports").child(listTransportSnapshot.getKey()).updateChildren(updateMap);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                //CAMBIARE QUI DEVE ANDARE A MY RESERVATION DELL CUSTOMER
                Intent intent = new Intent(reservation_summary.this, Homepage.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}