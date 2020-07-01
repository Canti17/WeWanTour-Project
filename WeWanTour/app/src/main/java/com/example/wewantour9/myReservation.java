package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class myReservation extends AppCompatActivity {

    DrawerLayout drawer;
    //NavigationView nav_view;
    //ActionBarDrawerToggle toggle;

    ViewPager viewpager;
    TabLayout tab;
    TabItem firstitem;
    TabItem seconditem;

    Pager_my_Reservation_Adapter pageradapter;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewpager = findViewById(R.id.viewpager);
        tab = findViewById(R.id.tabLayout);
        firstitem = findViewById(R.id.firstitem);
        seconditem = findViewById(R.id.seconditem);

        drawer = findViewById(R.id.drawerMyReservation);
        //nav_view = findViewById(R.id.nav_view);

        //toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);

        //drawer.addDrawerListener(toggle);
        //toggle.setDrawerIndicatorEnabled(true);
        //toggle.syncState();

        pageradapter = new Pager_my_Reservation_Adapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tab.getTabCount());
        viewpager.setAdapter(pageradapter);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                int g = tab.getPosition();
                String l = Integer.toString(g);
/*
                ViewPager.LayoutParams layoutparams = new ViewPager.LayoutParams();
                layoutparams.height = 500;
                ViewPager.LayoutParams layoutparams2 = new ViewPager.LayoutParams();
                layoutparams2.height = 1000;

                Log.d("ue", l);
                if(g == 0){
                    viewpager.setLayoutParams(layoutparams);
                }
                else{
                    viewpager.setLayoutParams(layoutparams2);
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }



}
