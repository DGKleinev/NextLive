package com.example.nextlive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.nextlive.model.EventoModel;
import com.example.nextlive.model.UserModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String USER_EMAIL = "utente_email";
    public static final String USER_ID = "utente_id";
    private static final String TAG = "MyActivity";

    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    View header_navigationView;
    TextView utenteHeader, emailHeader;
    private String myUserName;
    private ListView listView;
    private ArrayList<EventoModel> eventList = new ArrayList<>();
    private ListViewAdapter listViewAdapter = new ListViewAdapter();
    private DatabaseReference userDatabase;
    private DatabaseReference eventiDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*----------------Hooks-------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        header_navigationView = navigationView.getHeaderView(0);
        utenteHeader = header_navigationView.findViewById(R.id.headerUser_user);
        emailHeader = header_navigationView.findViewById(R.id.headerUser_email);

        userDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(getIntent().getStringExtra("utente_id"));
        eventiDatabase = FirebaseDatabase.getInstance().getReference().child("eventi");
        listView = findViewById(R.id.lista_home);
        emailHeader.setText(getIntent().getStringExtra("utente_email"));

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

        //In questa collection sono presenti tutti gli utenti e le rispettive informazioni
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel user = snapshot.getValue(UserModel.class);
                    myUserName = user.getUsername();
                    utenteHeader.setText(myUserName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        //In questa collection sono presenti tutti gli eventi e le rispettive informazioni
        eventiDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(MainActivity.this, "sono entrato qui: ", Toast.LENGTH_SHORT).show();
                Map<String,HashMap<String, String>> mappaParent = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                ArrayList<String> valoriEvento = new ArrayList<>();
                for(Map.Entry<String, HashMap<String, String>> entry : mappaParent.entrySet()) {
                    HashMap <String, String> value = entry.getValue();
                    EventoModel em = new EventoModel(
                            "autore",
                            "titolo",
                            "descrizione",
                            "indirizzo",
                            "data",
                            "genere",
                            "url"
                    );
                    for(Map.Entry <String, String> getEvento : value.entrySet()){
                        valoriEvento.add(getEvento.getValue());
                        switch (getEvento.getKey()){
                            case "idAutore":
                                em.setIdAutore(getEvento.getValue());
                                break;
                            case "titoloEvento":
                                em.setTitoloEvento(getEvento.getValue());
                                break;
                            case "descrizioneEvento":
                                em.setDescrizioneEvento(getEvento.getValue());
                                break;
                            case "indirizzoEvento":
                                em.setIndirizzoEvento(getEvento.getValue());
                                break;
                            case "dataEvento":
                                em.setDataEvento(getEvento.getValue());
                                break;
                            case "genereMusicale":
                                em.setGenereMusicale(getEvento.getValue());
                                break;
                            case "urlImmagineEvento":
                                em.seturlImmagineEvento(getEvento.getValue());
                                break;
                        }
                    }
                    eventList.add(em);
                }
                caricaInformazioni();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        //Listener che al essere selezionata porta al evento corrispettivo
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventoModel post = eventList.get(position);
                Toast.makeText(MainActivity.this, "hai clickato: " + post.getTitoloEvento(), Toast.LENGTH_SHORT).show();
            }
        });

        utenteHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //il clickListener funziona, non funziona la chiamata alla activity
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_ID,getIntent().getStringExtra("utente_id"));
                startActivity(intent);
            }
        });

    }

    //Hamburguer Menu
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
                intent.putExtra(PubblicaEventoActivity.USER_EMAIL, getIntent().getStringExtra("utente_email"));
                intent.putExtra(PubblicaEventoActivity.USER_ID, getIntent().getStringExtra("utente_id"));
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

    public void caricaInformazioni() {
        listViewAdapter = new ListViewAdapter(MainActivity.this, eventList);
        listView.setAdapter(listViewAdapter);
    }

}