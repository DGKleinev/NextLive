package com.example.nextlive;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.nextlive.adapter.EventListViewAdapter;
import com.example.nextlive.adapter.SingerListViewAdapter;
import com.example.nextlive.model.EventoModel;
import com.example.nextlive.model.GlideApp;
import com.example.nextlive.model.UserFanModel;
import com.example.nextlive.model.UserLocalModel;
import com.example.nextlive.model.UserSingerModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
    public static final String USER_EMAIL = "utente_email";
    public static final String USER_ID = "utente_id";
    public static final String USER_TYPE = "utente_tipo";
    private static final String TAG = "MyActivity";

    //Variables
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView immagineProfilo;
    private ListView listView;
    private ArrayList<EventoModel> eventList = new ArrayList<>();
    private ArrayList<UserSingerModel> singerList = new ArrayList<>();
    private EventListViewAdapter eventListViewAdapter = new EventListViewAdapter();
    private SingerListViewAdapter singerListViewAdapter = new SingerListViewAdapter();
    private DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*----------------Hooks-------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        View header_navigationView = navigationView.getHeaderView(0);
        TextView utenteHeader = header_navigationView.findViewById(R.id.headerUser_user);
        TextView emailHeader = header_navigationView.findViewById(R.id.headerUser_email);
        immagineProfilo = header_navigationView.findViewById(R.id.headerUser_image);

        userDatabase = FirebaseDatabase.getInstance().getReference().child("user");
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
        caricamentoInformazioniUtente();

        //In questa collection sono presenti tutti gli utenti e le rispettive informazioni
        //prendo le informazioni dell'utente loggato
        userDatabase.child(getIntent().getStringExtra("utente_id")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getIntent().getStringExtra("utente_tipo").equalsIgnoreCase("LOCALE")) {
                    UserLocalModel user = snapshot.getValue(UserLocalModel.class);
                    utenteHeader.setText(user.getNome());
                    loadHeadUserImage(user.getImgProfilo());
                }else{
                    UserFanModel user = snapshot.getValue(UserFanModel.class);
                    utenteHeader.setText(user.getUsername());
                    loadHeadUserImage(user.getImgProfilo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        //dentro l'if si trovano tutti i cantanti registrati
        //dentro l'else si trovano tutti gli eventi pubblicati
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getIntent().getStringExtra("utente_tipo").equalsIgnoreCase("LOCALE")) {
                    UserSingerModel post = singerList.get(position);
                    Toast.makeText(HomeActivity.this, "hai clickato: " + post.getUsername(), Toast.LENGTH_SHORT).show();
                }else{
                    EventoModel post = eventList.get(position);
                    Intent intent = new Intent(HomeActivity.this, CandidaturaActivity.class);
                   // https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android/37774966
                    Gson gson = new Gson();
                    String myJson = gson.toJson(post);
                    intent.putExtra("myjson", myJson);
                    intent.putExtra(PubblicaEventoActivity.USER_EMAIL, getIntent().getStringExtra("utente_email"));
                    intent.putExtra(PubblicaEventoActivity.USER_ID, getIntent().getStringExtra("utente_id"));
                    intent.putExtra(PubblicaEventoActivity.USER_TYPE, getIntent().getStringExtra("utente_tipo"));
                    startActivity(intent);
                }
            }
        });

        //Informazioni Utente loggato, quando clicco uno dei tre attributi mi porta al mio profilo
        utenteHeader.setOnClickListener(this);
        emailHeader.setOnClickListener(this);
        immagineProfilo.setOnClickListener(this);
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
                intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

            //Per Fan
            case R.id.nav_preferiti:
                intent = new Intent(HomeActivity.this, LocaliPreferitiActivity.class);
                startActivity(intent);
                break;

          /*  case R.id.nav_memo:
                intent = new Intent(HomeActivity.this, LocaliPreferitiActivity.class);
                startActivity(intent);
                break;
*/
            //Per Cantanti
  /*
            case R.id.nav_locali_preferiti:
                intent = new Intent(HomeActivity.this, LocaliPreferitiActivity.class);
                startActivity(intent);
                break;
*/
            case R.id.nav_proposte_inviate:
                intent = new Intent(HomeActivity.this, ProposteInviateActivity.class);
                startActivity(intent);
                break;
/*
            case R.id.nav_esibizione:
                intent = new Intent(HomeActivity.this, CandidaturaActivity.class);
                startActivity(intent);
                break;
*/
            //Per Locali
            case R.id.nav_cantanti_preferiti:
                intent = new Intent(HomeActivity.this, PreferitiActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_pubblica_eventi:
                intent = new Intent(HomeActivity.this, PubblicaEventoActivity.class);
                intent.putExtra(PubblicaEventoActivity.USER_EMAIL, getIntent().getStringExtra("utente_email"));
                intent.putExtra(PubblicaEventoActivity.USER_ID, getIntent().getStringExtra("utente_id"));
                intent.putExtra(PubblicaEventoActivity.USER_TYPE, getIntent().getStringExtra("utente_tipo"));
                startActivity(intent);
                break;

            case R.id.nav_eventi_creati:
                intent = new Intent(HomeActivity.this, EventiActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_vicino_me:
                intent = new Intent(HomeActivity.this, LocaliViciniActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Carica una lista di tutti i locali o cantanti nella home (a seconda di che utente sei)
    public void caricaInformazioni(String tipo) {
        if(tipo.equalsIgnoreCase("locale")) {
            eventListViewAdapter = new EventListViewAdapter(HomeActivity.this, eventList);
            listView.setAdapter(eventListViewAdapter);
        }
        if(tipo.equalsIgnoreCase("cantante")){
            singerListViewAdapter = new SingerListViewAdapter(HomeActivity.this, singerList);
            listView.setAdapter(singerListViewAdapter);
        }
    }

    private void caricamentoInformazioniUtente(){
        switch (getIntent().getStringExtra("utente_tipo").toUpperCase()){
            case "LOCALE":
                caricaCantanti();
                navigationView.inflateMenu(R.menu.main_menu_locale);
                break;
            case "CANTANTE":
                caricaEventi();
                navigationView.inflateMenu(R.menu.main_menu_cantante);
                break;
            case "FAN":
                caricaEventi();
                navigationView.inflateMenu(R.menu.main_menu_user);
                break;
        }
    }

    //metodo che carica una lista di eventi creati dai locali, è una home
    private void caricaEventi(){
        //In questa collection sono presenti tutti gli eventi e le rispettive informazioni
        DatabaseReference eventiDatabase;
        eventiDatabase = FirebaseDatabase.getInstance().getReference().child("eventi");
        eventiDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,HashMap<String, String>> mappaParent = (HashMap<String, HashMap<String, String>>) snapshot.getValue();

                //provare a fare mappaParent.forEach((k,v))
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
                    //  mappaParent.forEach((k,v)->k,v);
                    for(Map.Entry <String, String> getEvento : value.entrySet()){
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
                            case "pimage":
                                em.setPImage(getEvento.getValue());
                                break;
                        }
                    }
                    eventList.add(em);
                }
                caricaInformazioni("locale");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    //Servono ai locali, questi possono vedere tutti i cantanti registrati nella nostra applicazione
    private void caricaCantanti() {
        DatabaseReference singerReference = FirebaseDatabase.getInstance().getReference().child("user");
        singerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, HashMap<String, String>> mappaParent = (HashMap<String, HashMap<String, String>>) snapshot.getValue();

                //provare a fare mappaParent.forEach((k,v))
                //String nome, String imgProfilo, String tipo, String dataNascita, String cognome, String username
                for (Map.Entry<String, HashMap<String, String>> entry : mappaParent.entrySet()) {
                    HashMap<String, String> value = entry.getValue();
                    UserSingerModel usm = new UserSingerModel(
                            "nome",
                            "imgProfilo",
                            "tipo",
                            "dataNascita",
                            "cognome",
                            "username"
                    );
                    //  mappaParent.forEach((k,v)->k,v);
                    for (Map.Entry<String, String> getCantanti : value.entrySet()) {
                        switch (getCantanti.getKey().toUpperCase()) {
                            case "NOME":
                                usm.setNome(getCantanti.getValue());
                                break;
                            case "IMGPROFILO":
                                usm.setImgProfilo(getCantanti.getValue());
                                break;
                            case "TIPO":
                                usm.setTipo(getCantanti.getValue());
                                break;
                            case "DATANASCITA":
                                usm.setDataNascita(getCantanti.getValue());
                                break;
                            case "COGNOME":
                                usm.setCognome(getCantanti.getValue());
                                break;
                            case "USERNAME":
                                usm.setUsername(getCantanti.getValue());
                                break;
                        }
                    }
                    if(usm.getTipo().equalsIgnoreCase("CANTANTE"))
                        singerList.add(usm);
                }
                caricaInformazioni("cantante");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadHeadUserImage(String path){
        StorageReference eventImageRef = FirebaseStorage.getInstance().getReference("userImages/"+ path);
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(this /* context */)
                .load(eventImageRef)
                .into(immagineProfilo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.headerUser_image:
            case R.id.headerUser_email:
            case R.id.headerUser_user:
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_EMAIL, getIntent().getStringExtra("utente_email"));
                intent.putExtra(ProfileActivity.USER_ID, getIntent().getStringExtra("utente_id"));
                intent.putExtra(ProfileActivity.USER_TYPE, getIntent().getStringExtra("utente_tipo"));
                startActivity(intent);
                break;
        }
    }
}