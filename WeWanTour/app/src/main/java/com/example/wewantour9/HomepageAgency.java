package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

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

    private int value;


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int data = DataHolder.getInstance().getData();
            if(data == 1){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("The pedometer is on. If you close the" +
                        " app it will stop working, are you sure you want" +
                        " to exit WeWanTour?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();


            }
            else{
                super.onBackPressed();
            }

        }

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    DataHolder.getInstance().setData(0);
                    finishAffinity();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked NOTHING HAPPENS
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_agency);

        fAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = fAuth.getCurrentUser();
        Log.e("HomepageAgency CURRENT USER", Objects.requireNonNull(currentUser.getEmail()));

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


        //VARIABLE TO SAY THAT I AM AN AGENCY
        DataHolder.getInstance().setAgencycustomer("agency");

        //TOOLBAR
        setSupportActionBar(toolbar);

        value = getIntent().getIntExtra("Google",0);

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
                Intent intent = new Intent(HomepageAgency.this, ProfileUser.class);
                intent.putExtra("Ueila", 1);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                Intent intento = new Intent(HomepageAgency.this, Homepage.class);
                if(value == 2){
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                    fAuth.signOut();
                    mGoogleSignInClient.signOut();
                    intento.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intento);
                    finish();
                    break;
                }
                else {
                    fAuth.signOut();
                    intento.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intento);
                    finish();
                    break;
                }
            case R.id.nav_reservations:
                startActivity(new Intent(HomepageAgency.this, my_past_incoming_reservation.class));
                break;
            case R.id.nav_pedometer:
                int data = DataHolder.getInstance().getData();
                if(data == 0) {
                    startActivity(new Intent(HomepageAgency.this, PedometerChoice.class));
                }
                else{
                    finish();
                }
                break;
            case R.id.nav_compass:
                startActivity(new Intent(HomepageAgency.this, Compass.class));
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
                startActivity(new Intent(HomepageAgency.this, List_tour_forAddTransport.class));
                break;
            case R.id.alltours:
                Intent intent = new Intent(HomepageAgency.this, Homepage.class);
                intent.putExtra("Ue", 1);
                startActivity(intent);
                break;
            case R.id.toursandtransports:
                startActivity(new Intent(HomepageAgency.this, my_tours_and_transport.class));
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nav_view.setCheckedItem(R.id.nav_home);
    }
}
