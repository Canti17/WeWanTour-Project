package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class Compass  extends AppCompatActivity implements SensorEventListener {

    private Toolbar toolbar_compass;

    private ImageView compass;
    private TextView text_compass;

    private SensorManager sensorManager;
    private Sensor accel;
    private Sensor magn;
    private float[] myLastMagnetic = new float[3];
    private float[] myLastAcceleration = new float[3];
    private final float[] myOrientation = new float[3];

    private float yaw = 0f;
    private float current_yaw = 0f;

    private final float[] rotationMatrix = new float[9];

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);


        toolbar_compass = findViewById(R.id.toolbar_compass);
        setSupportActionBar(toolbar_compass);
        getSupportActionBar().setTitle("Check Orientation");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        compass = findViewById(R.id.compass);
        text_compass = findViewById(R.id.text_compass);

        String text_compassstring = text_compass.getText().toString(); //We need this?

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magn = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        sensorManager.registerListener(Compass.this, accel, SensorManager.SENSOR_DELAY_NORMAL); //STARTING SENSOR
        sensorManager.registerListener(Compass.this, magn, SensorManager.SENSOR_DELAY_NORMAL); //STARTING SENSOR



    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            myLastAcceleration = sensorEvent.values.clone();

        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            myLastMagnetic = sensorEvent.values.clone();

        }

        boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, myLastAcceleration, myLastMagnetic);
        if(success){
            SensorManager.getOrientation(rotationMatrix, myOrientation);

            yaw = (float) Math.toDegrees(myOrientation[0]);
            yaw = (yaw+360)%360;

            //ANIMATION NOT CANVAS
            Animation animation = new RotateAnimation(-current_yaw, -yaw, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            current_yaw = yaw;

            animation.setDuration(500);
            animation.setRepeatCount(0);
            animation.setFillAfter(true);

            compass.startAnimation(animation);

        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magn = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        sensorManager.registerListener(Compass.this, accel, SensorManager.SENSOR_DELAY_NORMAL); //STARTING SENSOR
        sensorManager.registerListener(Compass.this, magn, SensorManager.SENSOR_DELAY_NORMAL); //STARTING SENSOR
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }




}