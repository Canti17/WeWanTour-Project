package com.example.wewantour9;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PedometerRun extends AppCompatActivity {

    Toolbar toolbar;
    Button stoppedometer;

    @Override
    public void onBackPressed() {
        String agencycustomer = DataHolder.getInstance().getAgencycustomer();
        if(agencycustomer == "customer") {
            startActivity(new Intent(PedometerRun.this, Homepage.class));
        }
        else{
            startActivity(new Intent(PedometerRun.this, HomepageAgency.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_run);

        toolbar = findViewById(R.id.toolbar);
        stoppedometer = findViewById(R.id.stoppedometer);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pedometro");

        //IMPLEMENTAZIONE PEDOMETRO CON GESTION ONPAUSE() AND ONRESUME()

        stoppedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHolder.getInstance().setData(0);
                startActivity(new Intent(PedometerRun.this, PedometerChoice.class));
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String agencycustomer = DataHolder.getInstance().getAgencycustomer();
        if(agencycustomer == "customer") {
            startActivity(new Intent(PedometerRun.this, Homepage.class));
        }
        else{
            startActivity(new Intent(PedometerRun.this, HomepageAgency.class));
        }
        return true;
    }
}