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
import java.util.List;
import java.util.Objects;

public class List_transport_for_prenotation extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private List_transport_for_prenotation_adapter mAdapter;
    private TextView noTransportLabel;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseReferenceTour;
    private List<Transport> transports;
    private LinearLayoutManager mLayoutManager;
    private Activity activity;

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
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressCircle = findViewById(R.id.progress_circle);
        transports = new ArrayList<Transport>();

        activity = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        final Tour selectedTour =  (Tour) getIntent().getSerializableExtra("Tour class for transport list for prenotation");
        final int selectedNumberOfPeople =  (int) getIntent().getSerializableExtra("Number of people to filter the transports");

        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("TRANSPORT");
        mDatabaseReferenceTour.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Transport upload = postSnapshot.getValue(Transport.class);
                    if(upload.getDestination().equals(selectedTour.getStartPlace()) && (upload.getMaxPeople()-upload.getCurrentPeople()) >= selectedNumberOfPeople){
                        transports.add(upload);
                    }
                }
                if(transports.isEmpty()){
                    noTransportLabel.setVisibility(View.VISIBLE);
                }else{
                    noTransportLabel.setVisibility(View.GONE);
                }
                mAdapter = new List_transport_for_prenotation_adapter(List_transport_for_prenotation.this, transports, activity);
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


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}
