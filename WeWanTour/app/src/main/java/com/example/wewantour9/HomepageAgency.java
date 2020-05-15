package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomepageAgency extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth fAuth;

    NavigationView nav_view;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    CardView newtour;
    CardView newtransport;
    CardView alltours;
    CardView toursandtransports;


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
        setContentView(R.layout.activity_homepage_agency);

        fAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = fAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer);

        newtour = findViewById(R.id.addtour);
        newtransport = findViewById(R.id.addtransport);
        alltours = findViewById(R.id.alltours);
        toursandtransports = findViewById(R.id.toursandtransports);

        newtour.setOnClickListener((View.OnClickListener) this);
        toursandtransports.setOnClickListener((View.OnClickListener) this);
        newtransport.setOnClickListener((View.OnClickListener) this);
        alltours.setOnClickListener((View.OnClickListener) this);

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

        menu.findItem(R.id.nav_login).setVisible(false);
        menu.findItem(R.id.nav_register).setVisible(false);
        menu.findItem(R.id.nav_reservations).setVisible(false);

        }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_login:
                break;
            case R.id.nav_register:
                break;
            case R.id.nav_profile:
                startActivity(new Intent(HomepageAgency.this, ProfileUser.class));
                break;
            case R.id.nav_logout:
                fAuth.signOut();
                startActivity(new Intent(HomepageAgency.this, Homepage.class));
                finish();
                break;
            case R.id.nav_reservations:

                break;
            case R.id.nav_credits:
                startActivity(new Intent(HomepageAgency.this, Credits.class));
                break;



        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addtour:
                startActivity(new Intent(HomepageAgency.this, add_tour.class));
                break;
            case R.id.addtransport:
                startActivity(new Intent(HomepageAgency.this, add_transport.class));
                break;
            case R.id.alltours:
                Intent intent = new Intent(HomepageAgency.this, Homepage.class);
                intent.putExtra("Ue", 1);
                startActivity(intent);
                break;
            case R.id.toursandtransports:
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nav_view.setCheckedItem(R.id.nav_home);
    }
}
