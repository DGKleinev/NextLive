package com.example.nextlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.nextlive.R;
import com.example.nextlive.model.EventoModel;
import com.example.nextlive.model.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<EventoModel> eventList;

    public ListViewAdapter(){}

    public ListViewAdapter(Context context, ArrayList<EventoModel> eventList){
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
            convertView = View.inflate(context, R.layout.lista_item, null);
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
        autore.setText(singleEvent.getIdAutore());
        citta.setText(singleEvent.getIndirizzoEvento());
        data.setText(singleEvent.getDataEvento());
        genere.setText(singleEvent.getGenereMusicale());
        StorageReference eventImageRef = FirebaseStorage.getInstance().getReference("eventpics/"+ singleEvent.getPImage());

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(this.context /* context */)
                .load(eventImageRef)
                .into(img_anteprima);
        return convertView;
    }
}
