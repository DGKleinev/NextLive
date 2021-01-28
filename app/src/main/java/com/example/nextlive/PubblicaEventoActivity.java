package com.example.nextlive;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.nextlive.adapter.GenereMusicaleAdapter;
import com.example.nextlive.model.GenereMusicaleModel;

import java.util.ArrayList;
import java.util.Calendar;

public class PubblicaEventoActivity extends AppCompatActivity {

    Toolbar return_toolbar;

    //DatePicker
    private TextView dataEvento;
    private DatePickerDialog.OnDateSetListener dataEventoSetListener;

    //Spinner
    private Spinner spinnerGenereMusicale;
    final String [] generi = {"Genere Musicale", "Blues", "Country", "Disco", "Electro", "Folk",
        "Funk", "Hip-Hop", "House", "Jazz", "Pop", "Rythm and Blues", "Rap", "Reggae", "Rock"};

    //IngaggioCantanteVisible
    private CheckBox ingaggio;
    private TextView cantanteText;
    private EditText cantanteInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubblica_evento);

        /*-------------------HOOKS-------------------*/
        return_toolbar = findViewById(R.id.toolbar_return);
        dataEvento = findViewById(R.id.dataEvento);
        spinnerGenereMusicale = findViewById(R.id.generi_musicali);
        ingaggio = findViewById(R.id.ingaggioCantanteVisible);
        cantanteInput = findViewById(R.id.visibleCantanteInput);
        cantanteText = findViewById(R.id.visibleCantanteText);

        /*----------------Tool Bar------------------*/
        setSupportActionBar(return_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*--------------SPINNER-----------------*/
        ArrayList<GenereMusicaleModel> listaGeneri = new ArrayList<>();
        for(int i = 0; i < generi.length; i++){
            GenereMusicaleModel elemento = new GenereMusicaleModel();
            elemento.setGenere(generi[i]);
            elemento.setSelected(false);
            listaGeneri.add(elemento);
        }
        com.example.nextlive.adapter.GenereMusicaleAdapter genereMusicaleAdapter = new com.example.nextlive.adapter.GenereMusicaleAdapter(PubblicaEventoActivity.this, 0, listaGeneri);

        spinnerGenereMusicale.setAdapter(genereMusicaleAdapter);


        /*-----------DatePickerDialog----------*/
        dataEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =  new DatePickerDialog(
                        PubblicaEventoActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dataEventoSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dataEventoSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String data = dayOfMonth+"/"+month+"/"+year;
                dataEvento.setText(data);
            }
        };

        //Ingaggio utente
        cantanteText.setVisibility(View.INVISIBLE);
        cantanteInput.setVisibility(View.INVISIBLE);
        ingaggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingaggio.isChecked()) {
                    cantanteInput.setVisibility(View.VISIBLE);
                    cantanteText.setVisibility(View.VISIBLE);
                }else{
                    cantanteText.setVisibility(View.INVISIBLE);
                    cantanteInput.setVisibility(View.INVISIBLE);
                }
            }
        });


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