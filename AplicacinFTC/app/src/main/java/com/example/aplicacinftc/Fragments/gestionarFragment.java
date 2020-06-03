package com.example.aplicacinftc.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toolbar;

import com.example.aplicacinftc.Adapters.pagerAdapter;
import com.example.aplicacinftc.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class gestionarFragment extends Fragment implements AdapterView.OnItemClickListener {

    private pagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private View view;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    public gestionarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_gestionar, container, false);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.toolbarPrinci);
        //setActionBar(myToolbar);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        adapter = new pagerAdapter(getFragmentManager(),this,tabLayout.getTabCount());

        tabLayout.addTab(tabLayout.newTab().setText("Usuarios"));
        tabLayout.addTab(tabLayout.newTab().setText("Grupos"));
        tabLayout.addTab(tabLayout.newTab().setText("Asignaturas"));

        setViewPager();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;

    }

    private void setViewPager(){
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        adapter = new pagerAdapter(getFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
