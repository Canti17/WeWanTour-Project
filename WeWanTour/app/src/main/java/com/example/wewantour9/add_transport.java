package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class add_transport extends AppCompatActivity implements
        View.OnClickListener{

    private TextView txtViewVehicle;
    private Button btnDatePicker, btnTimePicker, btnSubmit;
    private ImageButton btnBus, btnCar;
    private TextInputEditText txtDate, txtTime, txtStartCity, txtStartStreet, txtStartCivic, txtCost, txtMaxPeople, txtMinPeople, transportDestination;
    private TextInputLayout layoutTxtDate, layoutTxtTime;
    int mYear, mMonth, mDay, mHour, mMinute;
    int outYear, outMonthOfYear, outDayOfMonth, outMinute, outHourOfDay;
    private boolean btnBusPressed=false;
    private boolean btnCarPressed=false;
    private Transport newTransport;

    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference db, db_agency;

    private Boolean newIdFlagAlreadySelected = false;

    private String new_transport_id;
    private Tour selectedTour;

    private Toolbar toolbar;


    public String pad(int input) {
        String str = "";
        if (input >= 10) {
            str = Integer.toString(input);
        } else {
            str = "0" + Integer.toString(input);
        }
        return str;
    }

    /*public String getNextId(DataSnapshot postSnapshot) {
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
    }*/


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transport);

        btnDatePicker = findViewById(R.id.TransportDateButton);
        btnTimePicker = findViewById(R.id.TransportHourButton);
        btnBus = findViewById(R.id.BusBtn);
        btnCar = findViewById(R.id.CarBtn);
        btnSubmit = findViewById(R.id.TransportSubmit);
        txtDate = findViewById(R.id.TransportDay);
        txtTime = findViewById(R.id.TransportHour);
        txtStartCity = findViewById(R.id.TransportStartCity);
        txtStartStreet = findViewById(R.id.TransportStartStreet);
        txtStartCivic = findViewById(R.id.TransportStartCivic);
        txtCost = findViewById(R.id.TransportCost);
        txtMinPeople = findViewById(R.id.TransportMinPeople);
        txtMaxPeople = findViewById(R.id.TransportMaxPeople);
        layoutTxtDate = findViewById(R.id.HintMovementTransportDay);
        layoutTxtTime = findViewById(R.id.HintMovementTransportHour);
        txtViewVehicle = findViewById(R.id.TextTransportVehicle);
        transportDestination = findViewById(R.id.TransportDestination);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Transport");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        db= database.getInstance().getReference("TRANSPORT");
        db_agency = database.getInstance().getReference("USER/Agency");


        selectedTour =  (Tour) getIntent().getSerializableExtra("Tour class from ListTourInAgency");
        transportDestination.setText(selectedTour.getStartPlace());

        db.addValueEventListener(new ValueEventListener() {
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
                if(!newIdFlagAlreadySelected){
                    new_transport_id =  String.valueOf(id_progressive);
                    newIdFlagAlreadySelected = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //used only to reset the error appeared in the data and time after the submit push with the empty field
        txtDate.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtDate.setError(null);
            }
        });
        txtTime.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtTime.setError(null);
            }
        });

        //change vehicle button colors when pressed
        btnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnCarPressed){
                    btnCarPressed=false;
                    btnCar.setBackgroundResource(R.drawable.imgbutton_color1_blue_version);
                }
                btnBus.setBackgroundResource(R.drawable.modified_date_time_button);
                btnBusPressed=true;
                txtViewVehicle.setError(null);
            }
        });
        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnBusPressed) {
                    btnBusPressed = false;
                    btnBus.setBackgroundResource(R.drawable.imgbutton_color1_blue_version);
                }
                btnCar.setBackgroundResource(R.drawable.modified_date_time_button);
                btnCarPressed=true;
                txtViewVehicle.setError(null);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            outYear = year;
                            outMonthOfYear = monthOfYear;
                            outDayOfMonth = dayOfMonth;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnTimePicker) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,int minute) {

                            txtTime.setText(pad(hourOfDay) + ":" + pad(minute));
                            outHourOfDay = hourOfDay;
                            outMinute = minute;

                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }

        if (v == btnSubmit) {

            boolean check = true;

            if(txtStartCity.getText().toString().equalsIgnoreCase("")) {
                //txtStartLocation.setHint("please enter start location");//it gives user to hint
                txtStartCity.setError("please enter start city");//it gives user to info message
                check = false;
            }
            if(txtStartStreet.getText().toString().equalsIgnoreCase("")) {
                //txtStartLocation.setHint("please enter start location");//it gives user to hint
                txtStartStreet.setError("please enter start street");//it gives user to info message
                check = false;
            }
            if(txtDate.getText().toString().equalsIgnoreCase("")) {
                //txtDate.setHint("please enter start date");//it gives user to hint
                txtDate.setError("please enter start date");//it gives user to info message
                check = false;
            }
            if(txtTime.getText().toString().equalsIgnoreCase("")) {
                //txtTime.setHint("please enter start houre");//it gives user to hint
                txtTime.setError("please enter start houre");//it gives user to info message
                check = false;
            }
            if(txtCost.getText().toString().equalsIgnoreCase("")) {
                //txtCost.setHint("please enter cost");//it gives user to hint
                txtCost.setError("please enter cost");//it gives user to info message
                check = false;
            }
            if(txtMaxPeople.getText().toString().equalsIgnoreCase("")) {
                //txtMaxPeople.setHint("please enter maximum number of people");//it gives user to hint
                txtMaxPeople.setError("please enter maximum number of people");//it gives user to info message
                check = false;
            }
            if(btnBusPressed==false && btnCarPressed==false){
                txtViewVehicle.setError("please select one from the proposed vehicles");//it gives user to info message
                check = false;
            }
            if(txtStartCivic.getText().toString().equalsIgnoreCase("") && check) {
                txtStartCivic.setText("SNC");
            }
            if(txtMinPeople.getText().toString().equalsIgnoreCase("") && check) {
                txtMinPeople.setText("1");
            }
            if(check) {
                String startDate = txtDate.getText().toString();
                String startHoure = txtTime.getText().toString();
                String buffer = txtMaxPeople.getText().toString();
                int intMaxPeople = Integer.parseInt(buffer);
                buffer = txtMinPeople.getText().toString();
                int intMinPeople = Integer.parseInt(buffer);
                double doubleCost = Double.parseDouble(txtCost.getText().toString());
                String startingLocation = txtStartCity.getText().toString()+", "+txtStartStreet.getText().toString()+", "+txtStartCivic.getText().toString();
                String vehicle="";
                if(btnBusPressed==true && btnCarPressed==false){
                    vehicle="Bus";
                }else if(btnBusPressed==false && btnCarPressed==true){
                    vehicle="Car";
                }

                String destFromTour = selectedTour.getStartPlace();
                newTransport = new Transport(startingLocation, startDate, startHoure, intMinPeople, 0, intMaxPeople, doubleCost, vehicle, destFromTour, null);

                db_agency.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Agency agency_crnt = postSnapshot.getValue(Agency.class);
                            if(agency_crnt.getEmail().equals(currentUser.getEmail())) {
                                newTransport.setAgency(agency_crnt.getEmail());
                                db.child(String.valueOf(new_transport_id)).setValue(newTransport);
                                //db_agency.child(postSnapshot.getKey()).child("list_transports").child(getNextId(postSnapshot.child("list_transports"))).setValue(newTransport);
                                db_agency.child(postSnapshot.getKey()).child("list_transports").child(new_transport_id).setValue(newTransport); //QUESTA RIGA VA SOSTITUITA ALLA PRECEDENTE QUANDO DECIDIAMO DI NON CANCELLARE PIU COSE A CAVOLO, SERVE AD AVERE UNA CONGRUENZA NEL DB TRA GLI ID /TRANSPORT & /USER/Agency/list_transports WHEN THIS LINE USED DELETE THE FUNCTION "getNextId" ABOVE

                                String text = "Transport uploaded";
                                Spannable centeredText = new SpannableString(text);
                                centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                        0, text.length() - 1,
                                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                Toast toast = Toast.makeText(add_transport.this, centeredText, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                startActivity(new Intent(getApplicationContext(), HomepageAgency.class));
                finish();
            }
        }
    }


}
