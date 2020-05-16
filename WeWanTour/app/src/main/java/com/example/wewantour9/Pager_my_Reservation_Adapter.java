package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Pager_my_Reservation_Adapter extends FragmentPagerAdapter {


    private int tabs;


    public Pager_my_Reservation_Adapter(@NonNull FragmentManager fm, int behavior, int tabs) {
        super(fm, behavior);
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new fragment_myTours_agency();
            case 1:
                return new fragment_myTransports_agency();

            default: return null;
        }

    }

    @Override
    public int getCount() {
        return tabs;
    }
}
