package com.example.nextlive;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.nextlive.adapter.ListViewAdapter;
import com.example.nextlive.model.ListaItemModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    View header_navigationView;
    TextView utente;

    private ListView listView;
    private ArrayList<ListaItemModel> listaItemModels = new ArrayList<>();
    private ListViewAdapter listViewAdapter;



    ListaItemModel prova1 = new ListaItemModel(R.mipmap.profile, "cantante 1", "milano", "reggae");
    ListaItemModel prova2 = new ListaItemModel(R.mipmap.profile, "cantante 2", "venezia", "folk");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*----------------Hooks-------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        header_navigationView = navigationView.getHeaderView(0);
        utente = header_navigationView.findViewById(R.id.profilo_utente);
        listView = findViewById(R.id.lista_home);

        /*----------------Tool Bar------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*--------------Navigation Draver Menu-----------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

       // navigationView.setCheckedItem(R.id.nav_impostazioni);

        //hide or show items
        //nasconde, in questo caso, mappa ed eventi
        Menu menu =  navigationView.getMenu();
      //  menu.findItem(R.id.nav_mappa).setVisible(false);
       // menu.findItem(R.id.nav_Eventi).setVisible(false);

        listaItemModels.add(prova1);
        listaItemModels.add(prova2);

        listViewAdapter = new ListViewAdapter(MainActivity.this, listaItemModels);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListaItemModel itemModel = listaItemModels.get(position);

                Toast.makeText(MainActivity.this, "hai clickato: " + itemModel.getTitolo(), Toast.LENGTH_SHORT).show();
            }
        });

        utente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //il clickListener funziona, non funziona la chiamata alla activity
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_impostazioni:

                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_locali_preferiti:
                intent = new Intent(MainActivity.this, LocaliPreferitiActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_cantanti_preferiti:
                intent = new Intent(MainActivity.this, PreferitiActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_pubblica:
                intent = new Intent(MainActivity.this, PubblicaEventoActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_eventi:
                intent = new Intent(MainActivity.this, EventiActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_proposte_inviate:
                intent = new Intent(MainActivity.this, ProposteInviateActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_mappa:
                intent = new Intent(MainActivity.this, LocaliViciniActivity.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}