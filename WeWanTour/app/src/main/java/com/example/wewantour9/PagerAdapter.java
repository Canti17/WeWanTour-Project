package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {


    private int tabs;


    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs) {
        super(fm, behavior);
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new UserFragmentRegistration();
            case 1:
                return new AgencyFragmentRegistration();

            default: return null;
        }

    }

    @Override
    public int getCount() {
        return tabs;
    }
}
