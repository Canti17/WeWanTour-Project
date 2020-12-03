package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;




public class PedometerChoice extends AppCompatActivity {

    Toolbar toolbar;
    Button startbutton;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_choice);
        toolbar = findViewById(R.id.toolbar);
        startbutton = findViewById(R.id.startbutton);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*GESTIONE ACTIVITIES
        int data = DataHolder.getInstance().getData();
        Log.d("ProvaStampa", String.valueOf(data));
        */

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHolder.getInstance().setData(1);
                startActivity(new Intent(PedometerChoice.this, PedometerRun.class));
                finish();

            }
        });

    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }


}