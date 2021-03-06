package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PedometerChoice extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    Button startbutton;
    private TextInputEditText height_p;
    private TextInputEditText weight_p;

    private DatabaseReference mDatabaseReferenceTour;
    private List<Reservation> reservations;
    private List<String> reservationsname;
    private SpinnerAdapter mAdapter;

    private FirebaseAuth fAuth;
    FirebaseUser current_user;

    String reservationselected;

    private Spinner spinner;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();


        setContentView(R.layout.activity_pedometer_choice);
        toolbar = findViewById(R.id.toolbar);
        startbutton = findViewById(R.id.startbutton);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        reservations = new ArrayList<Reservation>();
        reservationsname = new ArrayList<String>();

        height_p = findViewById(R.id.height_field);
        weight_p = findViewById(R.id.weight_field);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pedometer");



        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("RESERVATION");
        mDatabaseReferenceTour.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservations.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reservation reserv = postSnapshot.getValue(Reservation.class);
                    if(current_user.getEmail().equals(reserv.getCustomer())){
                        Date today = new Date();
                        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                        String todaynew= DateFor.format(today);
                        String[] tour_date_splitted= reserv.getTour().getStartDate().split("/");
                        String tour_date;
                        if(Integer.parseInt(tour_date_splitted[1])<10){
                            tour_date=tour_date_splitted[0]+"/"+"0"+tour_date_splitted[1]+"/"+tour_date_splitted[2];
                        }else{
                            tour_date=tour_date_splitted[0]+"/"+tour_date_splitted[1]+"/"+tour_date_splitted[2];
                        }

                        if(tour_date.equals(todaynew.toString())) {
                            reservations.add(reserv);
                            reservationsname.add(reserv.getTour().getName());
                        }

                    }
                }
                //mAdapter = new SpinnerAdapter(PedometerChoice.this, reservations)};
                Log.i("EEEE", reservationsname.toString());
                if (reservationsname.isEmpty()){
                    Toast.makeText(PedometerChoice.this, "You have not Reservations for today!", Toast.LENGTH_LONG).show();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PedometerChoice.this,
                        android.R.layout.simple_spinner_item, reservationsname);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PedometerChoice.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        /*GESTIONE ACTIVITIES
        int data = DataHolder.getInstance().getData();
        Log.d("ProvaStampa", String.valueOf(data));*/
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (reservationselected == null) {
                    Toast.makeText(PedometerChoice.this, "You have not selected a correct Tour!", Toast.LENGTH_LONG).show();

                }else{

                    Reservation res = SearchReservation(reservationselected);
                    Date today = new Date();
                    SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                    String todaynew= DateFor.format(today);
                    String[] tour_date_splitted= res.getTour().getStartDate().split("/");
                    String tour_date;
                    if(Integer.parseInt(tour_date_splitted[1])<10){
                        tour_date=tour_date_splitted[0]+"/"+"0"+tour_date_splitted[1]+"/"+tour_date_splitted[2];
                    }else{
                        tour_date=tour_date_splitted[0]+"/"+tour_date_splitted[1]+"/"+tour_date_splitted[2];
                    }
                    
                    if(!tour_date.equals(todaynew.toString())) {
                    Toast.makeText(PedometerChoice.this, "Error: This Tour is not starting today!!", Toast.LENGTH_LONG).show();
                    }
                     else if (res.getTour().getVehicle().equals("bike")) {
                        Toast.makeText(PedometerChoice.this, "Sorry,you are in bike!! The pedometer is not provided for this Tour.", Toast.LENGTH_LONG).show();
                    } else if (height_p.getText().toString().equals("")) {
                        Toast.makeText(PedometerChoice.this, "Insert your Height!!", Toast.LENGTH_SHORT).show();
                    } else if (weight_p.getText().toString().equals("")) {
                        Toast.makeText(PedometerChoice.this, "Insert your Weight!!", Toast.LENGTH_SHORT).show();
                    } else {

                        String name = res.getTour().getName();
                        double timetot = res.getTour().getDuration();
                        double kmtot = res.getTour().getTripLength();
                        DataHolder.getInstance().setData(1);
                        Intent intentrun = new Intent(PedometerChoice.this, PedometerRun.class);
                        intentrun.putExtra("Height", height_p.getText().toString());
                        intentrun.putExtra("Weight", weight_p.getText().toString());
                        intentrun.putExtra("Name", name);
                        intentrun.putExtra("Timetot", timetot);
                        intentrun.putExtra("Km", kmtot);

                        startActivity(intentrun);
                        finish();
                    }
                }


                }
                

        });


    }

    private Reservation SearchReservation(String reservationselected) {
        Reservation res = null;
        for (Reservation rest : reservations) {
            if(rest.getTour().getName().equals(reservationselected)){
                res = rest;
            }

        }
        return res;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        reservationselected = (String) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}