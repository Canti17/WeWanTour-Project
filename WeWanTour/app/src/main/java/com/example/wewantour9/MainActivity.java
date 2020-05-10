package com.example.wewantour9;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button btn_registration_user= (Button)findViewById(R.id.registration_user);
        btn_registration_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity_Registration_User.class));
            }
        });

        Button btn_registration_agency= (Button)findViewById(R.id.registration_agency);
        btn_registration_agency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity_Registration_Agency.class));
            }
        });

        Button btn_add_services = (Button)findViewById(R.id.add_services);
        btn_add_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, add_services.class));
            }
        });
        Button btn_prove = (Button)findViewById(R.id.prove);
        btn_prove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, prove.class));
            }
        });

    }
}
