package com.example.wewantour9;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {


    private int tabs;
    private String email;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs, String email) {
        super(fm, behavior);
        this.tabs = tabs;
        this.email = email;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("key", email);
                UserFragmentRegistration us = new UserFragmentRegistration();
                us.setArguments(bundle);
                return us;
            case 1:
                Bundle bundle2 = new Bundle();
                bundle2.putString("key", email);
                AgencyFragmentRegistration ag = new AgencyFragmentRegistration();
                ag.setArguments(bundle2);
                return ag;

            default: return null;
        }

    }

    @Override
    public int getCount() {
        return tabs;
    }
}
