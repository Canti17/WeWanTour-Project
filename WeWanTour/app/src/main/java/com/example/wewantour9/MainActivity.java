package com.example.wewantour9;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = fAuth.getCurrentUser();

        Button btn_login_user= (Button)findViewById(R.id.login);
        Button btn_registration_user= (Button)findViewById(R.id.registration_user);
        Button btn_registration_agency= (Button)findViewById(R.id.registration_agency);
        Button btn_add_services = (Button)findViewById(R.id.add_services);
        Button btn_prove = (Button)findViewById(R.id.prove);
        Button logout_button = findViewById(R.id.logout_button);

        if(currentUser != null){
            btn_login_user.setVisibility(View.INVISIBLE);
            btn_registration_user.setVisibility(View.INVISIBLE);
            btn_registration_agency.setVisibility(View.INVISIBLE);
        } else{
            btn_add_services.setVisibility(View.INVISIBLE);
            btn_prove.setVisibility(View.INVISIBLE);
            logout_button.setVisibility(View.INVISIBLE);
        }


        btn_login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        btn_registration_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity_Registration_User.class));
            }
        });

        btn_registration_agency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity_Registration_Agency.class));
            }
        });

        btn_add_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, add_services.class));
            }
        });

        btn_prove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Homepage.class));
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish(); };
        });

    }
}
