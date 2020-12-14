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
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;


public class PedometerChoice extends AppCompatActivity {

    Toolbar toolbar;
    Button startbutton;
    private TextInputEditText height_p;
    private TextInputEditText weight_p;

    private Spinner spinner;

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
        spinner = findViewById(R.id.spinner);

        height_p = findViewById(R.id.height_field);
        weight_p = findViewById(R.id.weight_field);

        final String height = height_p.getText().toString();
        final String weight = weight_p.getText().toString();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pedometro");

/*GESTIONE ACTIVITIES
        int data = DataHolder.getInstance().getData();
        Log.d("ProvaStampa", String.valueOf(data));
        */

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHolder.getInstance().setData(1);
                Intent intentrun = new Intent(PedometerChoice.this, PedometerRun.class);
                intentrun.putExtra("Height", height);
                intentrun.putExtra("Weight", weight);

                startActivity(intentrun);
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