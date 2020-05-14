package com.example.wewantour9;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth fAuth;

    NavigationView nav_view;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = fAuth.getCurrentUser();

        Button btn_login_user = (Button) findViewById(R.id.login);
        Button btn_registration_user = (Button) findViewById(R.id.registration_user);
        Button btn_registration_agency = (Button) findViewById(R.id.registration_agency);
        Button btn_add_services = (Button) findViewById(R.id.add_services);
        Button btn_prove = (Button) findViewById(R.id.prove);
        Button logout_button = findViewById(R.id.logout_button);
        Button alternativeregistration = findViewById(R.id.possiblealternativeregistration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer);

        //TOOLBAR
        setSupportActionBar(toolbar);


        //NAVIGATION MENU
        nav_view.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);
        nav_view.setCheckedItem(R.id.nav_home);


        //Hide or show items
        Menu menu = nav_view.getMenu();




        if (currentUser != null) {
            btn_login_user.setVisibility(View.INVISIBLE);
            btn_registration_user.setVisibility(View.INVISIBLE);
            btn_registration_agency.setVisibility(View.INVISIBLE);
            alternativeregistration.setVisibility(View.INVISIBLE);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_register).setVisible(false);

        } else {
            btn_add_services.setVisibility(View.INVISIBLE);
            btn_prove.setVisibility(View.INVISIBLE);
            logout_button.setVisibility(View.INVISIBLE);
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_reservations).setVisible(false);

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


        alternativeregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TotalRegister.class));
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            ;
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_login:
                startActivity(new Intent(MainActivity.this, Login.class));
                break;
            case R.id.nav_register:
                startActivity(new Intent(MainActivity.this, TotalRegister.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, ProfileUser.class));
                break;
            case R.id.nav_logout:
                fAuth.signOut();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.nav_reservations:
                break;
            case R.id.nav_credits:
                break;



        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
