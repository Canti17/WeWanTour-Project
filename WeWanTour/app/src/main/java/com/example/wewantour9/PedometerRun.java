package com.example.wewantour9;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PedometerRun extends AppCompatActivity {

    private Toolbar toolbar;
    private Button stoppedometer;
    ProgressBar progress;
    private int progr;
    private TextView nometour;
    private TextView goaltext;
    private int goal;

    private FirebaseAuth fAuth;
    FirebaseUser current_user;

    private Chronometer chrono;

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

        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        stoppedometer = findViewById(R.id.stoppedometer);
        progress= findViewById(R.id.progress_bar);
        nometour = findViewById(R.id.nometour);
        goaltext = findViewById(R.id.goal);



        //UPDATE STEP PROGRESS BAR
        //progr = progress.getProgress();
        //progress.setProgress(30);

        chrono = findViewById(R.id.timenumber);
        chrono.start();
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.setTypeface(ResourcesCompat.getFont(this, R.font.baloo));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pedometro");

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("Name");
        String height = extras.getString("Height");
        String weight = extras.getString("Weight");
        double timetot = extras.getDouble("Timetot");
        double kmtot = extras.getDouble("Km");

        Date today = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        String todaynew= DateFor.format(today);

        CharSequence newnometour = nometour.getText();
        CharSequence namenew = (CharSequence) name;

        newnometour = namenew +"  -  " + todaynew;
        nometour.setText(newnometour);
        goal = 9000;
        String goalstring =String.valueOf(goal);
        goaltext.setText("Goal: "+ goalstring + " steps");


        Log.d("EEEEE",height);
        Log.d("EEEEEE",weight);
        int stride = createStep(height, weight);

        //IMPLEMENTAZIONE PEDOMETRO CON GESTIONE ONPAUSE() AND ONRESUME()





        stoppedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chrono.stop();
                DataHolder.getInstance().setData(0);
                startActivity(new Intent(PedometerRun.this, PedometerChoice.class));
                finish();
            }
        });

    }

    private int createStep(String height, String weight) {
        int stridelocal = 0;
        int hht;

        if(weight.equals("default")){   //WEIGHT OPTIONAL NOT MESSO
            stridelocal -=8;

            hht = Integer.parseInt(height);
            stridelocal +=  (int) (hht*0.414);
        }

        else{
            hht = Integer.parseInt(height);
            int wht = Integer.parseInt(weight);

            stridelocal +=  (int) (hht*0.414);

        }




        return stridelocal;
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