package com.example.nextlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.nextlive.R;
import com.example.nextlive.model.EventoModel;
import com.example.nextlive.model.GlideApp;
import com.example.nextlive.model.UserLocalModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class EventListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<EventoModel> eventList;

    public EventListViewAdapter(){}

    public EventListViewAdapter(Context context, ArrayList<EventoModel> eventList){
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.lista_eventi, null);
        }
        ImageView img_anteprima = convertView.findViewById(R.id.img_anteprima);
        TextView titolo = convertView.findViewById(R.id.titolo);
        TextView autore = convertView.findViewById(R.id.autore);
        TextView citta = convertView.findViewById(R.id.citta);
        TextView data = convertView.findViewById(R.id.data);
        TextView genere = convertView.findViewById(R.id.genere);

        //Visualizzazione dati presi dal DB
        EventoModel singleEvent = eventList.get(position);
        titolo.setText(singleEvent.getTitoloEvento());
        citta.setText(singleEvent.getIndirizzoEvento());
        data.setText(singleEvent.getDataEvento());
        genere.setText(singleEvent.getGenereMusicale());
        StorageReference eventImageRef = FirebaseStorage.getInstance().getReference("eventpics/"+ singleEvent.getPImage());

        //grazie a singleEvent.getIdAutore() posso associare gli eventi ai locali che li hanno creati e con questo codice
        //prendo l'username
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(singleEvent.getIdAutore());
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserLocalModel user = snapshot.getValue(UserLocalModel.class);
                autore.setText(user.getNome());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(this.context /* context */)
                .load(eventImageRef)
                .into(img_anteprima);
        return convertView;
    }
}
