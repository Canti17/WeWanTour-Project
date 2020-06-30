package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private tour_adapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseReferenceTour;
    private List<Tour> mUploads;
    private LinearLayoutManager mLayoutManager;
    private Toolbar toolbar;
    private Menu menu;


    private FirebaseAuth fAuth;
    private  FirebaseAuth fAuth2;
    FirebaseUser current_user;
    FirebaseUser current;

    private NavigationView nav_view;
    private ActionBarDrawerToggle toggle;
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
        setContentView(R.layout.activity_homepage);

        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<Tour>();

        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer);




        //TOOLBAR
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int value = intent.getIntExtra("Ue", 0);
        Log.d("VALUE", Integer.toString(value));

        if(value == 1){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        else{

            //NAVIGATION MENU
            nav_view.bringToFront();
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
            drawer.addDrawerListener(toggle);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();


            nav_view.setNavigationItemSelectedListener(this);
            nav_view.setCheckedItem(R.id.nav_home);

            //Hide or show items
            menu = nav_view.getMenu();

            //GESTIONE MENU
            if(current_user == null) {
                menu.findItem(R.id.nav_logout).setVisible(false);
                menu.findItem(R.id.nav_profile).setVisible(false);
                menu.findItem(R.id.nav_reservations).setVisible(false);
            }
            else{
                menu.findItem(R.id.nav_login).setVisible(false);
                menu.findItem(R.id.nav_register).setVisible(false);

            }



        }



        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("TOUR");
        mDatabaseReferenceTour.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Tour upload = postSnapshot.getValue(Tour.class);
                    mUploads.add(upload);
                }
                mAdapter = new tour_adapter(Homepage.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Homepage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_login:
                startActivity(new Intent(Homepage.this, Login.class));
                break;
            case R.id.nav_register:
                startActivity(new Intent(Homepage.this, TotalRegister.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(Homepage.this, ProfileUser.class));
                break;
            case R.id.nav_logout:
                fAuth.signOut();
                startActivity(new Intent(Homepage.this, Homepage.class));
                finish();
                break;
            case R.id.nav_reservations:
                startActivity(new Intent(Homepage.this, My_reservation_customer.class));
                break;
            case R.id.nav_credits:
                startActivity(new Intent(Homepage.this, Credits.class));
                break;



        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onResume(){
        super.onResume();
        nav_view.setCheckedItem(R.id.nav_home);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
