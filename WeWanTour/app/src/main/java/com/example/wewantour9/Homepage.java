package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.ActionMenuItemView;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    private RecyclerView mRecyclerView;
    private tour_adapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseReferenceTour;
    private List<Tour> mUploads;
    private LinearLayoutManager mLayoutManager;
    private Toolbar toolbar;
    private Menu menu;
    private FloatingActionButton floatingButton;

    private ArrayList<tour_adapter> allrecyclers;

    private MaterialSearchView searchView;


    private FirebaseAuth fAuth;
    private  FirebaseAuth fAuth2;
    FirebaseUser current_user;
    FirebaseUser current;

    private NavigationView nav_view;
    private ActionBarDrawerToggle toggle;
    DrawerLayout drawer;

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
                        " to go out?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


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
        setContentView(R.layout.activity_homepage);

        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();
        if(current_user != null) Log.e("Homepage CURRENT USER", current_user.getEmail());

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

        floatingButton = findViewById(R.id.floatingButton);

        searchView = findViewById(R.id.search_view);

        value =  getIntent().getIntExtra("Google",0);


        //VARIABLE TO SAY THAT I AM AN USER
        DataHolder.getInstance().setAgencycustomer("customer");


        //TOOLBAR
        setSupportActionBar(toolbar);

        final Activity activity = this;

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                filter(query);
                hideKeyboard(activity);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

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

            //PEDOMETER IS ACTIVE ??
            int data = DataHolder.getInstance().getData();
            if(data == 0){
                floatingButton.findViewById(R.id.floatingButton).setVisibility((View.GONE));
            }

            //GESTIONE MENU
            if(current_user == null) {
                menu.findItem(R.id.nav_logout).setVisible(false);
                menu.findItem(R.id.nav_profile).setVisible(false);
                menu.findItem(R.id.nav_reservations).setVisible(false);
                menu.findItem(R.id.nav_pedometer).setVisible(false);

            }
            else{
                menu.findItem(R.id.nav_login).setVisible(false);
                menu.findItem(R.id.nav_register).setVisible(false);

            }



        }


        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();   //GO TO PEDOMETER PAGE
            }
                                          });


        mDatabaseReferenceTour = FirebaseDatabase.getInstance().getReference("TOUR");
        mDatabaseReferenceTour.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();

                //check if the current date is after the tour starting date
                final Calendar c = Calendar.getInstance();
                int current_year = c.get(Calendar.YEAR);
                int current_month = c.get(Calendar.MONTH)+1;
                int current_day = c.get(Calendar.DAY_OF_MONTH);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Tour upload = postSnapshot.getValue(Tour.class);
                    String[] dateSplit = upload.getStartDate().split("/");
                    if( !((current_year>Integer.parseInt(dateSplit[2])) ||
                            ((current_year == Integer.parseInt(dateSplit[2])) && current_month > Integer.parseInt(dateSplit[1])) ||
                            ((current_year == Integer.parseInt(dateSplit[2])) && current_month == Integer.parseInt(dateSplit[1]) && current_day > Integer.parseInt(dateSplit[0]))) ){
                        mUploads.add(upload);
                    }
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

    private void filter(String newText) {
        ArrayList<Tour> filterlist = new ArrayList<>();

        for(Tour item : mUploads){
            if (item.getName().toLowerCase().contains(newText.toLowerCase()) ){
                filterlist.add(item);
            }

        }

        mAdapter = new tour_adapter(Homepage.this, filterlist);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);


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
                startActivity(new Intent(Homepage.this, GoogleIntermediate.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(Homepage.this, ProfileUser.class));
                break;
            case R.id.nav_logout:
                if(value == 2){
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                    fAuth.signOut();
                    mGoogleSignInClient.signOut();
                    startActivity(new Intent(Homepage.this, Homepage.class));
                    finish();
                    break;
                }
                else {
                    fAuth.signOut();
                    startActivity(new Intent(Homepage.this, Homepage.class));
                    finish();
                    break;
                }
            case R.id.nav_reservations:
                startActivity(new Intent(Homepage.this, my_past_future_reservation.class));
                break;

            case R.id.nav_pedometer:
                int data = DataHolder.getInstance().getData();
                if(data == 0) {
                    startActivity(new Intent(Homepage.this, PedometerChoice.class));
                }
                else{
                    finish();
                }
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menutoolbar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    public void showpopup(MenuItem item) {

        View menuview = findViewById(R.id.sorting);
        PopupMenu popup = new PopupMenu(this, menuview);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.showpopupmenu);
        popup.show();


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.sortbyalph:
                Collections.sort(mUploads, Tour.TourNameComparator);
                mAdapter = new tour_adapter(Homepage.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                return true;

            case R.id.sortbycost:
                Collections.sort(mUploads, Tour.TourPriceComparator);
                mAdapter = new tour_adapter(Homepage.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                return true;

            case R.id.sortbydurat:
                Collections.sort(mUploads, Tour.TourDurationComparator);
                mAdapter = new tour_adapter(Homepage.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                return true;
        }

        return true;
    }
}
