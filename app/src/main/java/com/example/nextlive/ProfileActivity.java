package com.example.nextlive;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nextlive.adapter.GenereMusicaleAdapter;
import com.example.nextlive.adapter.SlidePagerAdapter;
import com.example.nextlive.model.GenereMusicaleModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity{
    public static final String USER_ID = "utente_id";
    View layout_toolbar;
    ImageView return_toolbar;
    TextView modifica_profilo;
    ViewPager2 pager;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        /*-------------------HOOKS-------------------*/
        //return_toolbar = findViewById(R.id.toolbar_return);
        layout_toolbar = findViewById(R.id.profilo_toolbar);
        return_toolbar = layout_toolbar.findViewById(R.id.toolbar_return);
        modifica_profilo = layout_toolbar.findViewById(R.id.profilo_modificaProfilo);
        database = FirebaseDatabase.getInstance().getReference().child("user").child(getIntent().getStringExtra("utente_id"));

        /*----------------Tool Bar------------------*/
        modifica_profilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ModificaProfiloActivity.class);
                intent.putExtra(ModificaProfiloActivity.USER_ID, getIntent().getStringExtra("utente_id"));
                startActivity(intent);
            }
        });
        return_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.USER_ID, getIntent().getStringExtra("utente_id"));
                startActivity(intent);
            }
        });

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