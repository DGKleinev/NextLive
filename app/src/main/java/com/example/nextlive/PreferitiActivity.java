package com.example.nextlive;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nextlive.adapter.ListViewAdapter;
import com.example.nextlive.model.EventoModel;
import com.example.nextlive.model.ListaItemModel;

import java.util.ArrayList;

public class PreferitiActivity extends AppCompatActivity {
    Toolbar return_toolbar;

    private ListView listView;
    private ArrayList<EventoModel> eventList = new ArrayList<>();
    private ListViewAdapter listViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferiti);

        /*-------------------HOOKS-------------------*/
        return_toolbar = findViewById(R.id.toolbar_return);
        /*----------------Tool Bar------------------*/
        setSupportActionBar(return_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listView = findViewById(R.id.lista_preferiti);
        //ListaItemModel prova1 = new ListaItemModel(R.mipmap.profile, "cantante 1", "milano", "reggae");
        //ListaItemModel prova2 = new ListaItemModel(R.mipmap.profile, "cantante 2", "venezia", "folk");
//
        //listaItemModels.add(prova1);
        //listaItemModels.add(prova2);

        listViewAdapter = new ListViewAdapter(PreferitiActivity.this, eventList);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventoModel post = eventList.get(position);

                Toast.makeText(PreferitiActivity.this, "hai clickato: " + post.getTitoloEvento(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}