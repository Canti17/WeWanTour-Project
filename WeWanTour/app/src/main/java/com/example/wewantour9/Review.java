package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.media.Rating;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Objects;

public class Review extends AppCompatActivity {

    private Toolbar toolbar;
    private Button sendReview;
    private RatingBar rating;
    private EditText text;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        sendReview = findViewById(R.id.sendReview);
        text = findViewById(R.id.textarea);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Review");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);




        rating = (RatingBar) findViewById(R.id.rating);
        //ratingBar.setRating(5);


        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(Review.this, String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
            }
        });


        sendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating.getRating() == 0.0){
                    Toast.makeText(Review.this, String.valueOf("Please, rate the tour from 0.5 to 5 stars!"),
                            Toast.LENGTH_SHORT).show();

                }

                else{
                    Toast.makeText(Review.this, String.valueOf("Need to send to Server!"),
                            Toast.LENGTH_SHORT).show();

                    float ratingValue = rating.getRating();
                    String testo = text.getText().toString().trim();

                    //SEND VALUES TO SERVER

                }

            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}