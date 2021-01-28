package com.example.nextlive.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.nextlive.R;

public class InformationFragment extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v =  inflater.inflate(R.layout.fragment_information, container, false);
        //return v;
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_information, container, false);

        return rootView;
    }
}