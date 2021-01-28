package com.example.nextlive.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.nextlive.R;

import java.util.ArrayList;

public class MultipleChoiceDialogFragment extends DialogFragment {
    public interface onMultiChoiceListener{
       void onPositiveButtonClicked(String[] list, ArrayList<String> selectedItemList);
       void onNegatveButtonClicked();
    }

    onMultiChoiceListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener=(onMultiChoiceListener) context;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassCastException(getActivity().toString()+"implementa must onMultiChoiceListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final ArrayList<String> selectdItemList= new ArrayList<>();

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        final String[] list=getActivity().getResources().getStringArray(R.array.Musica);

        builder.setTitle("seleziona la tua lista")
                .setMultiChoiceItems(list, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean b) {
                        if (b){
                            selectdItemList.add(list[i]);
                        }else {
                            selectdItemList.remove(list[i]);
                        }
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mListener.onPositiveButtonClicked(list, selectdItemList);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mListener.onNegatveButtonClicked();
                    }
                });
        return builder.create();
    }
}
