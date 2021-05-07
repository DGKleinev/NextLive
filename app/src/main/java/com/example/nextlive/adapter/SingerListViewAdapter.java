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
import com.example.nextlive.model.UserSingerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class SingerListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UserSingerModel> singerList;

    public SingerListViewAdapter(){}

    public SingerListViewAdapter(Context context, ArrayList<UserSingerModel> singerList){
        this.context = context;
        this.singerList = singerList;
    }

    @Override
    public int getCount() {
        return singerList.size();
    }

    @Override
    public Object getItem(int position) {
        return singerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.lista_cantanti, null);
        }
        ImageView img_anteprima = convertView.findViewById(R.id.img_anteprima);
        TextView username = convertView.findViewById(R.id.titolo_cantante);
        TextView citta = convertView.findViewById(R.id.citta_cantante);
        //TextView genere = convertView.findViewById(R.id.genere_musicale_cantante);

        //Visualizzazione dati presi dal DB
        UserSingerModel singer = singerList.get(position);
        username.setText(singer.getUsername());
        citta.setText(singer.getCitta());
        //genere.setText(singleEvent.getGenereMusicale());
        StorageReference singerImageRef = FirebaseStorage.getInstance().getReference("userImages/"+ singer.getImgProfilo());
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(this.context /* context */)
                .load(singerImageRef)
                .into(img_anteprima);
        return convertView;
    }
}
