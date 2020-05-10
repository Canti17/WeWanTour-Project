package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class add_transport extends AppCompatActivity implements
        View.OnClickListener{

    private TextView txtViewVehicle;
    private Button btnDatePicker, btnTimePicker, btnSubmit;
    private ImageButton btnBus, btnCar;
    private TextInputEditText txtDate, txtTime, txtStartCity, txtStartStreet, txtStartCivic, txtCost, txtMaxPeople;
    private TextInputLayout layoutTxtDate, layoutTxtTime;
    int mYear, mMonth, mDay, mHour, mMinute;
    int outYear, outMonthOfYear, outDayOfMonth, outMinute, outHourOfDay;
    //keep attention that there are no error in reload the page because these booleans are resetted outside the on create function
    private boolean btnBusPressed=false;
    private boolean btnCarPressed=false;

    private FirebaseDatabase database;
    private DatabaseReference db;

    int id = 0;


    public String pad(int input) {
        String str = "";
        if (input > 10) {
            str = Integer.toString(input);
        } else {
            str = "0" + Integer.toString(input);
        }
        return str;
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
        txtMaxPeople = findViewById(R.id.TransportMaxPeople);
        layoutTxtDate = findViewById(R.id.HintMovementTransportDay);
        layoutTxtTime = findViewById(R.id.HintMovementTransportHour);
        txtViewVehicle = findViewById(R.id.TextTransportVehicle);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
 /*
        //This is the code to directly show data and time in the edit text but is good only after the hint up movement
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        txtDate.setTextColor(ResourcesCompat.getColor(getResources(), R.color.hintTextColor, null));
        txtTime.setTextColor(ResourcesCompat.getColor(getResources(), R.color.hintTextColor, null));
        txtDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
        txtTime.setText(pad(mHour) + ":" + pad(mMinute));
*/

        db= database.getInstance().getReference("TRANSPORT");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    id= (int) dataSnapshot.getChildrenCount();
                }else{
                    ///
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
                    btnCar.setBackgroundResource(R.drawable.imgbutton_color1);
                }
                btnBus.setBackgroundResource(R.drawable.imgbutton_color2);
                btnBusPressed=true;
                txtViewVehicle.setError(null);
            }
        });
        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnBusPressed) {
                    btnBusPressed = false;
                    btnBus.setBackgroundResource(R.drawable.imgbutton_color1);
                }
                btnCar.setBackgroundResource(R.drawable.imgbutton_color2);
                btnCarPressed=true;
                txtViewVehicle.setError(null);
            }
        });

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

                            ////This is the code to directly show data and time in the edit text but is good only after the hint up movement
                            //layoutTxtDate.setHint("Starting Date");
                            //txtDate.setTextColor(ResourcesCompat.getColor(getResources(), R.color.blackTextColor, null));
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

                            ////This is the code to directly show data and time in the edit text but is good only after the hint up movement
                            //layoutTxtTime.setHint("Starting Time");
                            //txtTime.setTextColor(ResourcesCompat.getColor(getResources(), R.color.blackTextColor, null));
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
            if(txtStartCivic.getText().toString().equalsIgnoreCase("")) {
                //txtStartLocation.setHint("please enter start location");//it gives user to hint
                txtStartCivic.setError("please enter start civic, if no civic type SNC");//it gives user to info message
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
            if(check) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(outYear, outMonthOfYear, outDayOfMonth, outHourOfDay, outMinute);
                String buffer = txtMaxPeople.getText().toString();
                int intMaxPeople = Integer.parseInt(buffer);
                double doubleCost = Double.parseDouble(txtCost.getText().toString());
                String startingLocation = txtStartCity.getText().toString()+","+txtStartStreet.getText().toString()+","+txtStartCivic.getText().toString();
                String vehicle="";
                if(btnBusPressed==true && btnCarPressed==false){
                    vehicle="Bus";
                }else if(btnBusPressed==false && btnCarPressed==true){
                    vehicle="Car";
                }

                Transport transport = new Transport(startingLocation, calendar, 0, intMaxPeople, doubleCost, vehicle, null, null);        //DA CAMBIARE NEL MOMENTO DELLA CREAZIONE DELLA LIST DEI TOUR DELL'AGENT AGGIUNGI AGENCY
                db.child(String.valueOf(id)).setValue(transport);



                //Log.println(Log.ERROR, "2", transport.toString());
            }
        }
    }


}
