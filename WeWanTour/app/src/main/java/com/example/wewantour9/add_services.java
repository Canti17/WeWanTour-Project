package com.example.wewantour9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class add_services extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);


    ImageButton add_tour =findViewById(R.id.btn_AddTour);

        add_tour.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(add_services.this, add_tour.class));
        }
    });


    ImageButton btn_add_transport = findViewById(R.id.btn_AddTransport);
        btn_add_transport.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(add_services.this, add_transport.class));
        }
    });

    }

}
