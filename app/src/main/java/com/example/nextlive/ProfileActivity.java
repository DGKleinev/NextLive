package com.example.nextlive;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nextlive.adapter.SlidePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ProfileActivity extends AppCompatActivity{
    Toolbar return_toolbar;
    ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        /*-------------------HOOKS-------------------*/
        return_toolbar = findViewById(R.id.toolbar_return);

        /*----------------Tool Bar------------------*/
        setSupportActionBar(return_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*--- ViewPager 2---*/
        pager = findViewById(R.id.view_pager);
        pager.setAdapter(new com.example.nextlive.adapter.SlidePagerAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Informazioni");
                        break;
                    case 1:
                        tab.setText("Tracce");
                        break;
                    case 2:
                        tab.setText("Eventi");
                        break;
                    case 3:
                        tab.setText("Recensioni");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}