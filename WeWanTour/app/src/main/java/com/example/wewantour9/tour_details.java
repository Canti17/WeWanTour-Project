package com.example.wewantour9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class tour_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);

        Tour selectedTour =  (Tour) getIntent().getSerializableExtra("Tour class from HomePage");
        Log.println(Log.ERROR, "Current Tour:", selectedTour.toString());

    }
}
