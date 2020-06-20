package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Objects;

public class tour_details extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        finish();
    }

    private TextView tourTitle, startDate, startTime, startPlace, detailsText, duration, minPeople, nowPeople, maxPeople, cost;
    private ImageView vehicle, mainImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);

        mainImage = findViewById(R.id.imageViewMain);
        tourTitle = findViewById(R.id.textViewTourTitle);        //
        startDate = findViewById(R.id.textViewStartDate);       //
        startTime = findViewById(R.id.textViewStartTime);       //
        startPlace = findViewById(R.id.textViewStartPlace);     //
        detailsText = findViewById(R.id.textViewDetailsText);   //
        vehicle = findViewById(R.id.imageViewVehicle);
        duration = findViewById(R.id.textViewDurationValue);    //
        minPeople = findViewById(R.id.textViewMinPeople);       //
        nowPeople = findViewById(R.id.textViewNowPeople);       //
        maxPeople = findViewById(R.id.textViewMaxPeople);       //
        cost = findViewById(R.id.textViewCostValue);            //


        Tour selectedTour =  (Tour) getIntent().getSerializableExtra("Tour class from HomePage");
        Log.println(Log.ERROR, "Current Tour:", selectedTour.toString());

        Glide.with(getApplicationContext()).load(selectedTour.getFilePath()).into(mainImage);

        tourTitle.setText(selectedTour.getName());
        startDate.setText(selectedTour.getStartDate());
        startTime.setText(selectedTour.getStartHour());
        startPlace.setText(selectedTour.getStartPlace());
        detailsText.setText(selectedTour.getDescription());
        duration.setText(String.valueOf((int)selectedTour.getDuration())+" min");
        minPeople.setText(String.valueOf(selectedTour.getMinPeople()));
        nowPeople.setText(String.valueOf(selectedTour.getCurrentPeople()));
        maxPeople.setText(String.valueOf(selectedTour.getPeopleLimit()));
        double tourCost = selectedTour.getPrice();
        if((tourCost-(int)tourCost)!=0)
            cost.setText(String.valueOf(tourCost)+ " €");
        else
            cost.setText(String.valueOf((int)tourCost)+ " €");
        Drawable myDrawable;
        if(selectedTour.getVehicle().equals("bike"))
            myDrawable = getResources().getDrawable(R.drawable.bicycle);
        else
            myDrawable = getResources().getDrawable(R.drawable.walking_man);
        vehicle.setImageDrawable(myDrawable);




        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}
