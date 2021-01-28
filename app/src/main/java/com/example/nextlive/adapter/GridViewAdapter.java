package com.example.nextlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nextlive.R;
import com.example.nextlive.model.GridViewModel;

import java.util.ArrayList;


public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GridViewModel> lista;

    public GridViewAdapter(Context context, ArrayList<GridViewModel> lista){
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
            convertView = View.inflate(context, R.layout.grid_list_view, null);
        }
        ImageView img_anteprima = convertView.findViewById(R.id.image_grid);
        TextView titolo = convertView.findViewById(R.id.titolo_gridlist);

        GridViewModel gridModel = lista.get(position);
        img_anteprima.setImageResource(gridModel.getImageID());
        titolo.setText(gridModel.getTitolo());

        return convertView;
    }
}
