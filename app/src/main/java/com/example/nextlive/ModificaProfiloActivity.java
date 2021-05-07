package com.example.nextlive;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.nextlive.adapter.GenereMusicaleAdapter;
import com.example.nextlive.model.GenereMusicaleModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ModificaProfiloActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String USER_ID = "utente_id";
    View layout_toolbar;
    ImageView fotoProfilo, fotoCopertina;
    TextView return_toolbar, salvaModifiche;
    TextInputEditText nickname, descrizione, numero, website;
    DatabaseReference database;
    private Spinner spinnerGenereMusicale, spinnerTipoUtente;
    final String [] generi = {"Seleziona Genere Musicale", "Blues", "Country", "Disco", "Electro", "Folk",
            "Funk", "Hip-Hop", "House", "Jazz", "Pop", "Rythm and Blues", "Rap", "Reggae", "Rock"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_profilo);

        /*-------------------HOOKS-------------------*/
        layout_toolbar = findViewById(R.id.modificap_layout);
        return_toolbar = layout_toolbar.findViewById(R.id.modificap_txtannulla);
        salvaModifiche = layout_toolbar.findViewById(R.id.modificap_txtsalva);
        database = FirebaseDatabase.getInstance().getReference();
        spinnerGenereMusicale = findViewById(R.id.modifica_generiMusicali);
        spinnerTipoUtente = findViewById(R.id.modifica_tipoUtente);
        fotoProfilo = findViewById(R.id.modifica_user_image);
        fotoCopertina = findViewById(R.id.modifica_user_coverimage);
        nickname = findViewById(R.id.modifica_nome_utente);
        numero = findViewById(R.id.modifica_numero);
        website = findViewById(R.id.modifica_website);

        /*----------------SPINNER TIPO UTENTE----------------*/
        ArrayAdapter<String> utenteAdapter = new ArrayAdapter<>(this, R.layout.spinner_tipo_utente, R.id.spinner_txtTipoutente, getTipo());
        spinnerTipoUtente.setAdapter(utenteAdapter);

        /*----------------SPINNER MUSICALE----------------*/
        ArrayList<GenereMusicaleModel> listaGeneri = new ArrayList<>();
        for(int i = 0; i < generi.length; i++){
            GenereMusicaleModel elemento = new GenereMusicaleModel();
            elemento.setGenere(generi[i]);
            elemento.setSelected(false);
            listaGeneri.add(elemento);
        }
        GenereMusicaleAdapter genereMusicaleAdapter = new GenereMusicaleAdapter(ModificaProfiloActivity.this, 0, listaGeneri);
        spinnerGenereMusicale.setAdapter(genereMusicaleAdapter);

        /*----------------Tool Bar------------------*/
        return_toolbar.setOnClickListener(this);
        salvaModifiche.setOnClickListener(this);

        /*----------------MODIFICHE INPUT------------------*/
        fotoProfilo.setOnClickListener(this);
        fotoCopertina.setOnClickListener(this);

    }
//SPINNER
    private ArrayList<String> getTipo(){
        ArrayList<String> listaTipoUtente = new ArrayList<>();
        listaTipoUtente.add("Seleziona il tuo tipo utente");
        listaTipoUtente.add("Utente Normale");
        listaTipoUtente.add("Cantante");
        listaTipoUtente.add("Locale");
        return listaTipoUtente;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modificap_txtsalva:
                salvaCampiInput();
                break;
            case R.id.modificap_txtannulla:
                Intent intent = new Intent(ModificaProfiloActivity.this, ProfileActivity.class);
                intent.putExtra(HomeActivity.USER_ID, getIntent().getStringExtra("utente_id"));
                startActivity(intent);
                break;
        }
    }

    private void salvaCampiInput() {
        String user_nickname, user_descrizione, user_numero, user_website, user_generi;
        int num;
        user_nickname = nickname.getText().toString();
        user_descrizione = descrizione.getText().toString();
        user_numero = numero.getText().toString();
        user_website = numero.getText().toString();
        //database.child(id).push();
        //database.child("user").child(id).setValue(nUser);
        if (user_website.length() > 0) {
            if (!URLUtil.isValidUrl(user_website)) {
                Toast.makeText(this, "URL inserito non valido", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (spinnerGenereMusicale != null && spinnerGenereMusicale.getSelectedItem() != null) {
            user_generi = spinnerGenereMusicale.getSelectedItem().toString();
        } else
            user_generi = "";
        if (user_numero.length() < 6 || user_numero.length() > 13)
            num = 0;
        else
            num = Integer.parseInt(user_numero);

        if (user_nickname.length() < 3) {
            Toast.makeText(this, "il tuo nickname deve contenere almeno 3 caratteri", Toast.LENGTH_LONG).show();
            return;
        }
     /*
        if(spinnerGenereMusicale.getSelectedItem().toString().equalsIgnoreCase("Seleziona Genere Musicale"))

        if(spinnerTipoUtente.getSelectedItem().toString().equalsIgnoreCase("Utente Normale")){
            database.setValue();
        }
        else if(spinnerTipoUtente.getSelectedItem().toString().equalsIgnoreCase("Cantante")){
            database.child("cantante").orderByChild("ID").equalTo(getIntent().getStringExtra("utente_id")).once

            }
            CantanteModel cantante = new CantanteModel(user_descrizione,user_website,num,user_generi);
            database.child("cantante").child(getIntent().getStringExtra("utente_id")).setValue(cantante);
        }
        else if(spinnerTipoUtente.getSelectedItem().toString().equalsIgnoreCase("Locale")){

        }
        else{
            Toast.makeText(this, "Seleziona uno dei 3 tipi di utenti prima di continuare", Toast.LENGTH_LONG).show();
            return;
        }


    ()*/
    }
}