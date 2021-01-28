package com.example.nextlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nextlive.R;
import com.example.nextlive.model.ListaItemModel;

import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ListaItemModel> lista;

    public ListViewAdapter(Context context, ArrayList<ListaItemModel> lista){
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
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
        TextView citta = convertView.findViewById(R.id.citta);
        TextView informazione = convertView.findViewById(R.id.informazione1);

        ListaItemModel listModel = lista.get(position);
        img_anteprima.setImageResource(listModel.getImageId());
        titolo.setText(listModel.getTitolo());
        citta.setText(listModel.getCitta());
        informazione.setText(listModel.getPrima_informazione());

        return convertView;
    }
}
