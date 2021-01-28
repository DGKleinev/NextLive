package com.example.nextlive;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nextlive.adapter.GridViewAdapter;
import com.example.nextlive.model.GridViewModel;

import java.util.ArrayList;

public class ProposteInviateActivity extends AppCompatActivity {
    Toolbar return_toolbar;

    private GridView gridView;
    private ArrayList<GridViewModel> gridViewModels = new ArrayList<>();
    private com.example.nextlive.adapter.GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposte_inviate);

        /*-------------------HOOKS-------------------*/
        return_toolbar = findViewById(R.id.toolbar_return);
        /*----------------Tool Bar------------------*/
        setSupportActionBar(return_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        gridView = findViewById(R.id.lista_proposte);
        GridViewModel prova1 = new GridViewModel(R.mipmap.profile, "cantante 1");
        GridViewModel prova2 = new GridViewModel(R.mipmap.profile, "cantante 2");

        gridViewModels.add(prova1);
        gridViewModels.add(prova1);
        gridViewModels.add(prova1);
        gridViewModels.add(prova2);

        gridViewAdapter = new com.example.nextlive.adapter.GridViewAdapter(ProposteInviateActivity.this, gridViewModels);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridViewModel gridModel = gridViewModels.get(position);

                Toast.makeText(ProposteInviateActivity.this, "hai clickato: " + gridModel.getTitolo(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}