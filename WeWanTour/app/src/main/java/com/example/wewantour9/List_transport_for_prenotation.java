package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

public class List_transport_for_prenotation extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private List_transport_for_prenotation_adapter mAdapter;
    private TextView noTransportLabel, noTransportsForPeople;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseReferenceTour;
    private List<Transport> transports;
    private List<Transport> transportsFilteredByNumberPicker;
    private LinearLayoutManager mLayoutManager;
    private Activity activity;
    private NumberPicker numberPicker;

    private Toolbar toolbar;


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transport_for_prenotation);

        noTransportLabel = findViewById(R.id.textViewTransportListForCustomerBooking);
        noTransportsForPeople = findViewById(R.id.textViewTransportListForCustomerBookingNoSeats);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        numberPicker = findViewById(R.id.number_picker_transport);

        mProgressCircle = findViewById(R.id.progress_circle);
        transports = new ArrayList<Transport>();
        transportsFilteredByNumberPicker = new ArrayList<Transport>();

        activity = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        final Tour selectedTour =  (Tour) getIntent().getSerializableExtra("Tour class for transport list for prenotation");
        final int selectedNumberOfPeople =  (int) getIntent().getSerializableExtra("Number of people to filter the transports");

        numberPicker.setMin(1);
        numberPicker.setUnit(1);

        if (savedInstanceState != null) {
            numberPicker.setValue(savedInstanceState.getInt("numberPickerPreviousValue"));
        } else {
            numberPicker.setValue(selectedNumberOfPeople);
        }

        Log.e("NumberPickerValue", numberPicker.getValue()+"");

        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("TRANSPORT");

        mDatabaseReferenceTour.addValueEventListener(new ValueEventListener() {
            int maxTransportPeoples = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transports.clear();

                //check if the current date is after the tour starting date
                final Calendar c = Calendar.getInstance();
                int current_year = c.get(Calendar.YEAR);
                int current_month = c.get(Calendar.MONTH)+1;
                int current_day = c.get(Calendar.DAY_OF_MONTH);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Transport upload = postSnapshot.getValue(Transport.class);
                    int bufferTransportPeople = (upload.getMaxPeople()-upload.getCurrentPeople());

                    if(upload.getDestination().equals(selectedTour.getStartPlace())){
                        String[] dateSplit = upload.getStartDate().split("-");
                        if( !((current_year>Integer.parseInt(dateSplit[2])) ||
                                ((current_year == Integer.parseInt(dateSplit[2])) && current_month > Integer.parseInt(dateSplit[1])) ||
                                ((current_year == Integer.parseInt(dateSplit[2])) && current_month == Integer.parseInt(dateSplit[1]) && current_day > Integer.parseInt(dateSplit[0]))) ){

                            if(maxTransportPeoples < bufferTransportPeople){
                                maxTransportPeoples = bufferTransportPeople;
                            }
                            transports.add(upload);
                            if(bufferTransportPeople >= numberPicker.getValue()){
                                transportsFilteredByNumberPicker.add(upload);

                            }
                        }
                    }
                }
                if(transports.isEmpty()){
                    noTransportLabel.setVisibility(View.VISIBLE);
                    noTransportsForPeople.setVisibility(View.GONE);
                }else{
                    noTransportLabel.setVisibility(View.GONE);
                    if(transportsFilteredByNumberPicker.isEmpty()){
                        noTransportsForPeople.setVisibility(View.VISIBLE);
                    }else{
                        noTransportsForPeople.setVisibility(View.GONE);
                    }
                }
                //set the max of the number picker maximum number of seata available for the bigger transport
                numberPicker.setMax(maxTransportPeoples+1);
                mAdapter = new List_transport_for_prenotation_adapter(List_transport_for_prenotation.this, transportsFilteredByNumberPicker, activity);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(List_transport_for_prenotation.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int x, ActionEnum y) {
                transportsFilteredByNumberPicker.clear();
                for (Transport tr : transports) {
                    int bufferTransportPeople = (tr.getMaxPeople() - tr.getCurrentPeople());
                    if (bufferTransportPeople >= x) {
                        transportsFilteredByNumberPicker.add(tr);
                    }
                }
                if( !(transports.isEmpty()) ){
                    if(transportsFilteredByNumberPicker.isEmpty()){
                        noTransportsForPeople.setVisibility(View.VISIBLE);
                    }else{
                        noTransportsForPeople.setVisibility(View.GONE);
                    }
                }
                mAdapter = new List_transport_for_prenotation_adapter(List_transport_for_prenotation.this, transportsFilteredByNumberPicker, activity);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("numberPickerPreviousValue", numberPicker.getValue());
        super.onSaveInstanceState(savedInstanceState);
    }

}
