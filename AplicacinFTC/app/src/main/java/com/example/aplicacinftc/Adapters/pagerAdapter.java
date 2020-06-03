package com.example.aplicacinftc.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.aplicacinftc.Fragments.gestionarFragment;
import com.example.aplicacinftc.Fragments.tab1Fragment;
import com.example.aplicacinftc.Fragments.tab2Fragment;
import com.example.aplicacinftc.Fragments.tab3Fragment;

public class pagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;


    public pagerAdapter(FragmentManager fm, gestionarFragment context, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new tab1Fragment();
            case 1:
                return new tab2Fragment();
            case 2:
                return new tab3Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}