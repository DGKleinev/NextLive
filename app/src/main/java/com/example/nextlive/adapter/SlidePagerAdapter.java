package com.example.nextlive.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nextlive.fragment.InformationFragment;
import com.example.nextlive.fragment.Presentazioni_EventiFragment;
import com.example.nextlive.fragment.RecensioniFragment;
import com.example.nextlive.fragment.TracceFragment;


public class SlidePagerAdapter extends FragmentStateAdapter {

    public SlidePagerAdapter(FragmentActivity fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new InformationFragment();
            case 1:
                return new TracceFragment();
            case 2:
                return new Presentazioni_EventiFragment();
            default:
                return new RecensioniFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
