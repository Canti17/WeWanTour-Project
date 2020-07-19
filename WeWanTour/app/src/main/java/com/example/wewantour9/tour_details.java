package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class tour_details extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        finish();
    }

    private TextView tourTitle, startDate, startTime, startPlace, detailsText, duration, minPeople, nowPeople, maxPeople, cost, transDate, transHour, transPlace, transCost, noTransport, transDateLabel, transHourLabel, transPlaceLabel, transCostLabel, transVehicleLabel, directRegister;
    private ImageView vehicle, mainImage, transVehicle, deleteTransport;
    private Button selectTransport, gotToSummaryPage;
    private ConstraintLayout bookingOptionsLayout1, bookingOptionsLayout2;
    private NumberPicker numberPicker;
    private Tour selectedTour;
    private Transport selectedTransport;
    private Reservation newReservation = new Reservation();

    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        mainImage = findViewById(R.id.imageViewMain);
        tourTitle = findViewById(R.id.textViewTourTitle);
        startDate = findViewById(R.id.textViewStartDate);
        startTime = findViewById(R.id.textViewStartTime);
        startPlace = findViewById(R.id.textViewStartPlace);
        detailsText = findViewById(R.id.textViewDetailsText);
        vehicle = findViewById(R.id.imageViewVehicle);
        duration = findViewById(R.id.textViewDurationValue);
        minPeople = findViewById(R.id.textViewMinPeople);
        nowPeople = findViewById(R.id.textViewNowPeople);
        maxPeople = findViewById(R.id.textViewMaxPeople);
        cost = findViewById(R.id.textViewCostValue);
        selectTransport = findViewById(R.id.buttonTourDetailsAddTransport);
        numberPicker = findViewById(R.id.number_picker);
        transDate = findViewById(R.id.textViewTourDetailsTransportDateValue);
        transHour = findViewById(R.id.textViewTourDetailsTransportHourValue);
        transPlace = findViewById(R.id.textViewTourDetailsTransportPlaceValue);
        transCost = findViewById(R.id.textViewTourDetailsTransportCostValue);
        transVehicle = findViewById(R.id.imageViewTourDetailsTransportVehicleValue);
        noTransport = findViewById(R.id.textViewTourDetailsNoTransport);
        transDateLabel = findViewById(R.id.textViewTourDetailsTransportDate);
        transHourLabel = findViewById(R.id.textViewTourDetailsTransportHour);
        transPlaceLabel = findViewById(R.id.textViewTourDetailsTransportPlace);
        transCostLabel = findViewById(R.id.textViewTourDetailsTransportCost);
        transVehicleLabel = findViewById(R.id.textViewTourDetailsTransportVehicle);
        deleteTransport = findViewById(R.id.imageViewTourDetailsTransportRemove);
        gotToSummaryPage = findViewById(R.id.buttonTourDetailsGoToSummaryPage);
        directRegister = findViewById(R.id.textViewTourDetailsRegistrationDirectLink);
        bookingOptionsLayout1 = findViewById(R.id.constraintLayoutTourDetails6);
        bookingOptionsLayout2 = findViewById(R.id.constraintLayoutTourDetails3);

        selectedTour =  (Tour) getIntent().getSerializableExtra("Tour class from HomePage");

        Glide.with(getApplicationContext()).load(selectedTour.getFilePath()).into(mainImage);

        tourTitle.setText(selectedTour.getName());
        startDate.setText(selectedTour.getStartDate());
        startTime.setText(selectedTour.getStartHour());
        startPlace.setText(selectedTour.getStartPlace());
        detailsText.setText(selectedTour.getDescription());
        duration.setText(String.valueOf((int)selectedTour.getDuration())+" min");
        minPeople.setText(String.valueOf(selectedTour.getMinPeople()));
        nowPeople.setText(String.valueOf(selectedTour.getCurrentPeople()));
        maxPeople.setText(String.valueOf(selectedTour.getPeopleLimit()));
        double tourCost = selectedTour.getPrice();
        if((tourCost-(int)tourCost)!=0)
            cost.setText(String.valueOf(tourCost)+ " €");
        else
            cost.setText(String.valueOf((int)tourCost)+ " €");
        Drawable myDrawable;
        if(selectedTour.getVehicle().equals("bike"))
            myDrawable = getResources().getDrawable(R.drawable.bicycle);
        else
            myDrawable = getResources().getDrawable(R.drawable.walking_man);
        vehicle.setImageDrawable(myDrawable);


        selectTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tour_details.this, List_transport_for_prenotation.class);
                intent.putExtra("Tour class for transport list for prenotation", selectedTour);
                intent.putExtra("Number of people to filter the transports", numberPicker.getValue());
                startActivityForResult(intent,1);
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        directRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TotalRegister.class));
            }
        });

        if(currentUser==null){
            selectTransport.setClickable(false);
            //selectTransport.setBackgroundColor(Color.parseColor("grey"));
            gotToSummaryPage.setText("Login to book the tour");
            bookingOptionsLayout1.setVisibility(View.GONE);
            bookingOptionsLayout2.setVisibility(View.GONE);

            gotToSummaryPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(tour_details.this, GoogleIntermediate.class);
                    startActivity(intent);
                    finish();
                }
            });

        }else{
            //Choose number widget
            numberPicker.setMax(selectedTour.getPeopleLimit()-selectedTour.getCurrentPeople());
            numberPicker.setMin(1);
            numberPicker.setUnit(1);
            numberPicker.setValue(1);
            directRegister.setVisibility(View.GONE);

            gotToSummaryPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newReservation.setTransport(selectedTransport);
                    newReservation.setTour(selectedTour);
                    newReservation.setCustomer(currentUser.getEmail());
                    newReservation.setNumberOfPeople(numberPicker.getValue());
                    Intent intent = new Intent(tour_details.this, reservation_summary.class);
                    intent.putExtra("Reservation class from tour details to summary", newReservation);
                    startActivity(intent);
                }
            });

        }


        numberPicker.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int x, ActionEnum y) {
                    if(selectedTransport!=null){
                        int availableSeats = selectedTransport.getMaxPeople()-selectedTransport.getCurrentPeople();
                        if(availableSeats < numberPicker.getValue()){
                            new AlertDialog.Builder(tour_details.this)
                                    .setTitle("Warning")
                                    .setMessage("The maximum number of free seats available for the selected transport is "+ availableSeats)

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    /*.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                        }
                                    })*/

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(getResources().getDrawable(R.drawable.error))
                                    .show();
                                    numberPicker.setValue(numberPicker.getValue()-1);
                        }
                    }
                }
        });

        //Impostazione iniziale, nessuntrasporto selezionato
        transVehicle.setVisibility(View.INVISIBLE);
        transCost.setVisibility(View.INVISIBLE);
        transPlace.setVisibility(View.INVISIBLE);
        transDate.setVisibility(View.INVISIBLE);
        transHour.setVisibility(View.INVISIBLE);
        transVehicleLabel.setVisibility(View.INVISIBLE);
        transCostLabel.setVisibility(View.INVISIBLE);
        transPlaceLabel.setVisibility(View.INVISIBLE);
        transDateLabel.setVisibility(View.INVISIBLE);
        transHourLabel.setVisibility(View.INVISIBLE);
        deleteTransport.setVisibility(View.INVISIBLE);


        deleteTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transVehicle.setVisibility(View.INVISIBLE);
                transCost.setVisibility(View.INVISIBLE);
                transPlace.setVisibility(View.INVISIBLE);
                transDate.setVisibility(View.INVISIBLE);
                transHour.setVisibility(View.INVISIBLE);
                transVehicleLabel.setVisibility(View.INVISIBLE);
                transCostLabel.setVisibility(View.INVISIBLE);
                transPlaceLabel.setVisibility(View.INVISIBLE);
                transDateLabel.setVisibility(View.INVISIBLE);
                transHourLabel.setVisibility(View.INVISIBLE);
                selectTransport.setText("ADD TRANSPORT");
                noTransport.setVisibility(View.VISIBLE);
                deleteTransport.setVisibility(View.INVISIBLE);
                selectedTransport = null;
            }
        });

    }

    //Callback function from the transport list
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {

                    selectedTransport =  (Transport) data.getSerializableExtra("Transport from list Transports for booking");

                    noTransport.setVisibility(View.INVISIBLE);
                    transVehicle.setVisibility(View.VISIBLE);
                    transCost.setVisibility(View.VISIBLE);
                    transPlace.setVisibility(View.VISIBLE);
                    transDate.setVisibility(View.VISIBLE);
                    transHour.setVisibility(View.VISIBLE);
                    transVehicleLabel.setVisibility(View.VISIBLE);
                    transCostLabel.setVisibility(View.VISIBLE);
                    transPlaceLabel.setVisibility(View.VISIBLE);
                    transDateLabel.setVisibility(View.VISIBLE);
                    transHourLabel.setVisibility(View.VISIBLE);
                    deleteTransport.setVisibility(View.VISIBLE);

                    Drawable myDrawableTrans;
                    transDate.setText(selectedTransport.getStartDate());
                    transHour.setText(selectedTransport.getStartHour());
                    transPlace.setText(selectedTransport.getStartLocation());
                    double transportCost = selectedTransport.getCost();
                    if((transportCost-(int)transportCost)!=0) {
                        transCost.setText(String.valueOf(transportCost) + " €");
                    }else {
                        transCost.setText(String.valueOf((int) transportCost) + " €");
                    }
                    if(selectedTransport.getVehicle().equals("Bus")){
                        myDrawableTrans = getResources().getDrawable(R.drawable.bus);
                    }else{
                        myDrawableTrans = getResources().getDrawable(R.drawable.car);
                    }
                    transVehicle.setImageDrawable(myDrawableTrans);
                    selectTransport.setText("CHANGE TRANSPORT");
                }
                break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int res_id = item.getItemId();
        if (res_id == R.id.menu_info) {
            AlertDialog.Builder info = new AlertDialog.Builder(tour_details.this);
            info.setMessage("Remember: You can book a transport only for all the people in your group not only a part!");


            info.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });

            info.create().show();
        } else {

            finish();


        }


        return true;

    }


    }


