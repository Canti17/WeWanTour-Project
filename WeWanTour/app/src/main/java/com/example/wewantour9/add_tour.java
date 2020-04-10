package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class add_tour extends AppCompatActivity {

    private Button btn_DatePicker, btn_HourPicker, btn_submit;
    private ImageButton btn_bike, btn_walk;
    private EditText edtxt_name, edtxt_description, edtxt_startCity,edtxt_startStreet, edtxt_startCivic, edtxt_StartDate, edtxt_StartHour, edtxt_price,
            edtxt_duration, edtxt_currentPeople, edtxt_peopleLimit;
    private TextView txt_vehicle;
    private int outYear, outMonthOfYear, outDayOfMonth, outMinute, outHourOfDay;


    private boolean bike_pressed=false;
    private boolean walk_pressed=false;

    private FirebaseDatabase database;
    private DatabaseReference db;
    private int id=0;


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
        setContentView(R.layout.activity_add_tour);


        btn_DatePicker = findViewById(R.id.btn_startdate);
        btn_HourPicker = findViewById(R.id.btn_starthour);
        btn_submit= findViewById(R.id.btn_submit);
        btn_walk= findViewById(R.id.btn_AddTransport);
        btn_bike= findViewById(R.id.btn_AddTour);
        edtxt_description = findViewById(R.id.edittxt_description);
        edtxt_startCity = findViewById(R.id.edittxt_startcity);
        edtxt_startStreet = findViewById(R.id.edittxt_startstreet);
        edtxt_startCivic = findViewById(R.id.edittxt_startcivic);
        edtxt_name = findViewById(R.id.edittxt_name);
        edtxt_StartDate = findViewById(R.id.edittxt_startdate);
        edtxt_StartHour = findViewById(R.id.edittxt_starthour);
        edtxt_price = findViewById(R.id.edittxt_price);
        edtxt_duration = findViewById(R.id.edittxt_duration);
        edtxt_currentPeople = findViewById(R.id.edittxt_currentpeople);
        edtxt_peopleLimit= findViewById(R.id.edittxt_peoplelimit);
        txt_vehicle = findViewById(R.id.txt_vehicle);



        db= database.getInstance().getReference("TOUR");

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




        btn_DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog date_picker = new DatePickerDialog(add_tour.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtxt_StartDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                outYear = year;
                                outMonthOfYear = monthOfYear;
                                outDayOfMonth = dayOfMonth;
                            }
                        }, year, month, day);
                date_picker.show();
            }
        });


        btn_HourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar2 = Calendar.getInstance();
                int hour = calendar2.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar2.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog hour_picker = new TimePickerDialog(add_tour.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                edtxt_StartHour.setText(pad(sHour) + "." + pad(sMinute));
                                outHourOfDay = sHour;
                                outMinute = sMinute;
                            }
                        }, hour, minutes, true);
                hour_picker.show();

            }
        });


        btn_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bike_pressed){
                    bike_pressed=false;
                    btn_bike.setBackgroundResource(R.drawable.imgbutton_color1);
                }
                btn_walk.setBackgroundResource(R.drawable.imgbutton_color2);
                walk_pressed=true;
                txt_vehicle.setError(null);
            }
        });

        btn_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walk_pressed) {
                    walk_pressed = false;
                    btn_walk.setBackgroundResource(R.drawable.imgbutton_color1);
                }
                btn_bike.setBackgroundResource(R.drawable.imgbutton_color2);
                txt_vehicle.setError(null);
                bike_pressed=true;
            }
        });

        //used only to reset the error appeared in the data and time after the submit push with the empty field
        edtxt_StartDate.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtxt_StartDate.setError(null);
            }
        });
        edtxt_StartHour.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtxt_StartHour.setError(null);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if(edtxt_name.getText().toString().equalsIgnoreCase("")) {
                    edtxt_name.setError("please enter Tour name");//it gives user to info message)
                    check = false;
                }
                if(edtxt_description.getText().toString().equalsIgnoreCase("")) {
                    edtxt_description.setError("please enter Tour description");//it gives user to info message)
                    check = false;
                }
                if(edtxt_startCity.getText().toString().equalsIgnoreCase("")) {
                    edtxt_startCity.setError("please enter city");//it gives user to info message)
                    check = false;
                }
                if(edtxt_startStreet.getText().toString().equalsIgnoreCase("")) {
                    edtxt_startStreet.setError("please enter street");//it gives user to info message)
                    check = false;
                }
                if(edtxt_startCivic.getText().toString().equalsIgnoreCase("")) {
                    edtxt_startCivic.setError("please enter civic");//it gives user to info message)
                    check = false;
                }
                if(edtxt_StartDate.getText().toString().equalsIgnoreCase("")) {
                    edtxt_StartDate.setError("please enter start date");//it gives user to info message
                    check = false;
                }
                if(edtxt_StartHour.getText().toString().equalsIgnoreCase("")) {
                    edtxt_StartHour.setError("please enter start hour");//it gives user to info message
                    check = false;
                }
                if(edtxt_price.getText().toString().equalsIgnoreCase("")) {
                    edtxt_price.setError("please enter price");//it gives user to info message
                    check = false;
                }
                if(edtxt_duration.getText().toString().equalsIgnoreCase("")) {
                    edtxt_duration.setError("please duration ");//it gives user to info message
                    check = false;
                }
                if(edtxt_currentPeople.getText().toString().equalsIgnoreCase("")) {
                    edtxt_currentPeople.setError("please enter number of current people");//it gives user to info message
                    check = false;
                }
                if(edtxt_peopleLimit.getText().toString().equalsIgnoreCase("")) {
                    edtxt_peopleLimit.setError("please enter number of people limit");//it gives user to info message
                    check = false;
                }
                if(!((bike_pressed==true && walk_pressed==false) ||  (bike_pressed==false && walk_pressed==true))){
                    txt_vehicle.setError("please choose the vehicle");//it gives user to info message
                    check = false;
                }
                if(check){
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(outYear, outMonthOfYear, outDayOfMonth, outHourOfDay, outMinute);
                    double doublePrice = Double.parseDouble(edtxt_price.getText().toString());
                    double doubleDuration = Double.parseDouble(edtxt_duration.getText().toString());
                    String tourName= edtxt_name.getText().toString();
                    String tourDescription= edtxt_description.getText().toString();
                    String startPlace= edtxt_startCity.getText().toString()+","+edtxt_startStreet.getText().toString()+","+edtxt_startCivic.getText().toString();
                    int currentPeople= Integer.parseInt(edtxt_currentPeople.getText().toString());
                    int peopleLimit= Integer.parseInt(edtxt_peopleLimit.getText().toString());
                    String vehicle="";
                    if(bike_pressed==true && walk_pressed==false){
                        vehicle="bike";
                    }else if(bike_pressed==false && walk_pressed==true){
                        vehicle="walk";
                    }
                    Tour tour=new Tour(tourName,tourDescription,startPlace,calendar,doublePrice,doubleDuration,currentPeople,peopleLimit,vehicle);
                    db.child(String.valueOf(id)).setValue(tour);
                }
            }
        });

    }
}
