package com.example.wewantour9;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.pow;

public class PedometerRun extends AppCompatActivity implements SensorEventListener, StepListener {

    private Toolbar toolbar;
    private Button stoppedometer;
    ProgressBar progress;
    private TextView nometour;
    private TextView goaltext;
    private TextView stepnumber;
    private TextView caloriesnumber;
    private TextView kmnumber;
    private float goal;
    private final int KMTOCM = 100000;
    private float kmdone;

    private FirebaseAuth fAuth;
    FirebaseUser current_user;

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;
    private int stride;
    private String numberEqual;
    private double caloriescalculator;
    private int height_int;
    private int weight_int;
    double kmTot;
    double timeTot;

    private List<Float> spaceInterval;

    private Chronometer chrono;


    private int progressStatus = 0;
    private Handler handler = new Handler();

    private  My_Canvas_View canvas_view;
    private Canvas canvas;
    private graphView graphView;

    private boolean isMoving=false;
    private TextView percentage;
    private ProgressBar horiz_pb;


    private ObjectAnimator animation;

    private int x_ripetition;

    private boolean global_bool=false;
    private int temp=-1;

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
        stepnumber = findViewById(R.id.stepnumber);
        caloriesnumber = findViewById(R.id.caloriesnumber);
        kmnumber = findViewById(R.id.kmnumber);
        numberEqual = "";

        spaceInterval = new ArrayList<Float>();


        chrono = findViewById(R.id.timenumber);
        chrono.start();
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.setTypeface(ResourcesCompat.getFont(this, R.font.baloo));

        canvas_view=findViewById(R.id.canvas_view);
        canvas_view.setMoving(false);


        canvas=canvas_view.getCanvas();


        graphView=findViewById(R.id.graphView2);


        percentage = (TextView) findViewById(R.id.tv);
        horiz_pb = (ProgressBar) findViewById(R.id.pb);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pedometro");

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("Name");
        String height = extras.getString("Height");
        String weight = extras.getString("Weight");
        height_int = Integer.parseInt(height);
        weight_int = Integer.parseInt(weight);
        timeTot = extras.getDouble("Timetot");
        kmTot = extras.getDouble("Km");


        Log.d("STAMPO IN PEDOMETER RUN--------- KM-----",""+kmTot);

        graphView.setLunghezza_percorso((float) (kmTot*1000));
        graphView.setDurata_tour((float) timeTot);

        Date today = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        String todaynew= DateFor.format(today);

        CharSequence newnometour = nometour.getText();
        CharSequence namenew = (CharSequence) name;

        stride = createStep(height_int, weight_int);

        //STRINGS OF THE PAGE
        newnometour = namenew +"  -  " + todaynew;
        nometour.setText(newnometour);
        goal = (float)(kmTot*KMTOCM)/stride;  //CALCOLO TRA LO STRIDE(PASSO) e i KM da fare secondo l'agenzia
        int goalint = (int) goal;
        String goalstring = String.valueOf(goalint);
        goaltext.setText("Tour Goal: "+ goalstring + " steps");


        x_ripetition=1;


        //IMPLEMENTAZIONE PEDOMETRO CON GESTIONE ONPAUSE() AND ONRESUME()


        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        numSteps = 0;
        sensorManager.registerListener(PedometerRun.this, accel, SensorManager.SENSOR_DELAY_FASTEST); //STARTING SENSOR



        stoppedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chrono.stop();
                DataHolder.getInstance().setData(0);
                startActivity(new Intent(PedometerRun.this, PedometerChoice.class));
                finish();
            }
        });


        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                countIntervals();
            }
        },0,10000);



    }

    private void countIntervals() {

        x_ripetition+=1;
        float value = kmdone;   //ALL KM UNTIL NOW
        value = (float)value*1000;  //KM TO METERS
        Log.i("EEEE", String.valueOf(value));
        spaceInterval.add(value);   //ADD TO THE LIST

        graphView.addLine(x_ripetition,value);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        stepnumber.setText(String.valueOf(numSteps));  //UPDATE TEXT VALUE

        double AverageVelocity = 1.2;
        double AverageMetersForMinute = AverageVelocity*60;
        int AverageStepsforMinute = (int)(AverageMetersForMinute*100)/stride;

        caloriescalculator = ((double) 0.035 * weight_int) + (((double) pow(AverageVelocity, 2) /height_int) * ((double)0.029*weight_int));
        double caloriesnewInt = (double)caloriescalculator/4;
        int averageStepModified = AverageStepsforMinute/4;



        int old_progress=0;

        float calc = goal/100;
        canvas_view.setMoving(true);
        for(int i = 1; i<=100; i++){
            float number = calc*i;
            if(numSteps > number){
                progress.setProgress(i);  //UPDATE STEP PROGRESS BAR
                horiz_pb.setProgress(i);

                percentage.setText(i+"%");
                old_progress=i;
                /*float end = canvas_view.getTranslationX() + 1;
                animation = ObjectAnimator.ofFloat(canvas_view, "translationX", end);
                animation.setDuration(50);
                animation.start();*/

            }
            if(numSteps == averageStepModified*i){
                caloriesnewInt = caloriesnewInt*i;
                String caloriesnew = new DecimalFormat("#.##").format(caloriesnewInt); //"1.2"
                caloriesnumber.setText(caloriesnew); //UPDATE CALORIES 10 SECOND
            }


        }



        if(old_progress!=temp) {
            temp = old_progress;

            Log.d("STAMPO IL TEMP", ""+temp);


            if (old_progress != 0 && old_progress % 1 == 0) {

                canvas_view.setK_to2(1);
           /* if(old_progress<16) {
                canvas_view.setK_to2(1);
            }else if(old_progress>=16 && old_progress<31){
                canvas_view.setK_to2(3);
            }*/
            }

        }

        Log.d("STAMPO OLD_PROGRESS:", "" + old_progress);


        kmdone = ((float) numSteps*stride/100000);
        String kmdonenew = new DecimalFormat("#.##").format(kmdone); //"1.2"

        if(!numberEqual.equals(kmdonenew)){
            numberEqual = kmdonenew;
            kmnumber.setText(kmdonenew);
        }

        /*long millis = SystemClock.elapsedRealtime() - chrono.getBase();
        long second = millis/1000;
        Log.d("EEEEE", String.valueOf(second));*/






    }


    private int createStep(int hht, int wht) {
        int stridelocal = 0;
        //OTHER IDEAS INTERESTING
        if(wht > 100){
            stridelocal -=8;
        }
        stridelocal +=  (int) (hht*0.414);

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


    @Override
    protected void onResume() {
        super.onResume();
        canvas_view.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        canvas_view.pause();
    }
}