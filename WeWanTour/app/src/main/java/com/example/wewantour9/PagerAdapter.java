package com.example.wewantour9;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {


    private int tabs;
    private String email;
    private int value;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs, String email, int value) {
        super(fm, behavior);
        this.tabs = tabs;
        this.email = email;
        this.value = value;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("key", email);
                bundle.putInt("google",value);
                UserFragmentRegistration us = new UserFragmentRegistration();
                us.setArguments(bundle);
                return us;
            case 1:
                Bundle bundle2 = new Bundle();
                bundle2.putString("key", email);
                bundle2.putInt("google",value);
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
