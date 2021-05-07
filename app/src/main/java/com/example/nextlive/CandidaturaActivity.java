package com.example.nextlive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.nextlive.adapter.SingerListViewAdapter;
import com.example.nextlive.model.CandidaturaModel;
import com.example.nextlive.model.EventoModel;
import com.example.nextlive.model.UserLocalModel;
import com.example.nextlive.model.UserSingerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CandidaturaActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String USER_EMAIL = "utente_email";
    public static final String USER_ID = "utente_id";
    public static final String USER_TYPE = "utente_tipo";
    public static final String TAG = "";
    private String idEvento = "";
    private int listMaxSize = 1;
    View layout_toolbar;
    ImageView return_toolbar;
    private SingerListViewAdapter singerListViewAdapter = new SingerListViewAdapter();
    private ListView listView;
    private ArrayList<String> idCantanti = new ArrayList<>();
    private EventoModel eventoSelezionato = null;
    private DatabaseReference candidaturaDB;
    private TextView candidazione;
    private TextView nomeLocale;

    //DA LAVORARE SU QUESTA ACTIVITY: Possibiltà di aggiornare in tempo reale la visualizzazione della lista, questo significa
    //al momento che uno clicca dovrà entrare in gioco il metodo carica

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatura);

        Gson gson = new Gson();
        eventoSelezionato = gson.fromJson(getIntent().getStringExtra("myjson"), EventoModel.class);


        /*-------------------HOOKS-------------------*/
        layout_toolbar = findViewById(R.id.candidatura_toolbar);
        return_toolbar = layout_toolbar.findViewById(R.id.toolbar_return);
        DatabaseReference localeDb;
        candidaturaDB = FirebaseDatabase.getInstance().getReference().child("candidatura");
        TextView nomeEvento = findViewById(R.id.textToolbar_nomeEvento);
        nomeLocale = findViewById(R.id.candidatura_nome_locale);
        TextView descrizioneEvento = findViewById(R.id.candidatura_descrizione_evento);
        TextView genereMusicale = findViewById(R.id.candidatura_genere_musicale);
        TextView dataEvento = findViewById(R.id.candidatura_data_evento);
        candidazione = findViewById(R.id.btnCandidatura);

        listView = findViewById(R.id.lista_cantanti_candidati);

        //Cerco l'id della candidatura
        trovaCandidati();

        //Carico i dati del locale che ha creato l'evento
        String idAutoreEvento = eventoSelezionato.getIdAutore();
        localeDb = FirebaseDatabase.getInstance().getReference().child("user").child(idAutoreEvento);
        localeDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserLocalModel user = snapshot.getValue(UserLocalModel.class);
                nomeLocale.setText(user.getNome());
                nomeEvento.setText(eventoSelezionato.getTitoloEvento());
                descrizioneEvento.setText(eventoSelezionato.getDescrizioneEvento());
                genereMusicale.setText(eventoSelezionato.getGenereMusicale());
                dataEvento.setText(eventoSelezionato.getDataEvento());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });



        /*----------------Tool Bar------------------*/
        return_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CandidaturaActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.USER_ID, getIntent().getStringExtra("utente_id"));
                startActivity(intent);
            }
        });
        //Se mi sono già candidato all'evento selezionato allora il texto del bottone viene modificato
        candidaturaPresente();
        caricaDati();
        candidazione.setOnClickListener(this);
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

    //Carica una lista di tutti i cantanti che si sono candidati all'evento selezionato
    public void caricaInformazioni(ArrayList<UserSingerModel> singerList) {

        singerListViewAdapter = new SingerListViewAdapter(CandidaturaActivity.this, singerList);
        listView.setAdapter(singerListViewAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCandidatura:
                //ultima modifica: if
                if(candidazione.getText().toString().equalsIgnoreCase("Candidati")) {
                    registraCandidatura();
                  //  candidazione.setText("Disiscrizione");
                }else {
                    rimuoviCandidatura();
                }
                break;
        }
    }

    private void trovaCandidati(){
        DatabaseReference eventoDB = FirebaseDatabase.getInstance().getReference().child("eventi");
        Query query = eventoDB.orderByChild("dataEvento").equalTo(eventoSelezionato.getDataEvento());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    EventoModel em = ds.getValue(EventoModel.class);
                    if(em.equals(eventoSelezionato)){
                        idEvento = ds.getKey();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void registraCandidatura(){
        String currentCantante = getIntent().getStringExtra("utente_id");
        candidaturaDB.child(idEvento).addListenerForSingleValueEvent(new ValueEventListener() {
            GenericTypeIndicator<List<String>> cantanti = new GenericTypeIndicator<List<String>>() {};
            List<String> cantantiList;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    cantantiList = snapshot.getValue(cantanti);
                }
                else{
                    cantantiList = new ArrayList<String>();
                    candidaturaDB.child(idEvento).push();
                }
                cantantiList.add(currentCantante);
                candidaturaDB.child(idEvento).setValue(cantantiList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        candidazione.setText("Rimuovi Candidatura");
        caricaDati();
    }

    private void rimuoviCandidatura(){
        String currentCantante = getIntent().getStringExtra("utente_id");
        candidaturaDB.child(idEvento).addListenerForSingleValueEvent(new ValueEventListener() {
            GenericTypeIndicator<List<String>> cantanti = new GenericTypeIndicator<List<String>>() {};
            List<String> cantantiList;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cantantiList = snapshot.getValue(cantanti);
                cantantiList.remove(currentCantante);
                List<String> cantantiAggiornati = new ArrayList<>();
                cantantiAggiornati.addAll(cantantiList);
                candidaturaDB.child(idEvento).setValue(cantantiList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        candidazione.setText("Candidati");
        caricaDati();
    }



    private void caricaDati(){
        ArrayList<String> cantantiList = new ArrayList<>();
        ArrayList<UserSingerModel> cantantiListModel = new ArrayList<>();
        candidaturaDB.child(idEvento).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    GenericTypeIndicator<List<String>> cantanti = new GenericTypeIndicator<List<String>>() {};
                    List<String> cantantiRegistrati;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getKey().equalsIgnoreCase(idEvento)) {
                            cantantiRegistrati = ds.getValue(cantanti);
                            cantantiList.addAll(cantantiRegistrati);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        DatabaseReference cantantiDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = cantantiDB.orderByChild("tipo").equalTo("cantante");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    UserSingerModel cantante = ds.getValue(UserSingerModel.class);
                    if (cantantiList.contains(ds.getKey())) {
                        cantantiListModel.add(cantante);
                        cantantiList.remove(ds.getKey());

                        if (cantantiList.size() == 0){
                            caricaInformazioni(cantantiListModel);
                            return;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void candidaturaPresente(){
        String currentCantante = getIntent().getStringExtra("utente_id");
        candidaturaDB.child(idEvento).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    //serve a prendere dati di tipo List
                    GenericTypeIndicator<List<String>> cantanti = new GenericTypeIndicator<List<String>>() {};
                    List<String> cantantiList;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        cantantiList = ds.getValue(cantanti);
                        if(ds.getKey().equalsIgnoreCase(idEvento) && cantantiList.contains(currentCantante)){
                            candidazione.setText("Rimuovi Candidatura");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}