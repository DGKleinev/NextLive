package com.example.nextlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.nextlive.R;
import com.example.nextlive.model.GenereMusicaleModel;

import java.util.ArrayList;

public class GenereMusicaleAdapter extends ArrayAdapter<GenereMusicaleModel> {
    private Context context;
    private ArrayList<GenereMusicaleModel> listaGeneri;
    private GenereMusicaleAdapter genereMusicaleAdapter;
    private boolean isFromView = false;

    public GenereMusicaleAdapter(Context context, int resource, ArrayList<GenereMusicaleModel> generi) {
        super(context, resource, generi);
        this.context = context;
        this.listaGeneri = generi;
        this.genereMusicaleAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            convertView = layoutInflator.inflate(R.layout.spinner_genere_musicale, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
      holder.mTextView.setText(listaGeneri.get(position).getGenere());

        isFromView = true;
        holder.mCheckBox.setChecked(listaGeneri.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {

            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                if (!isFromView) {
                    listaGeneri.get(position).setSelected(isChecked);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}