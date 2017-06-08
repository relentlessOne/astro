package com.example.pc.astro;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class TimeLocalizationFragment extends Fragment {
    public TextView currentTime;
    public TextView localization;
    public TextView refreshRate;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_localization,container,false);
        currentTime = (TextView) view.findViewById(R.id.currentTime);
        localization = (TextView) view.findViewById(R.id.localization);
        refreshRate = (TextView) view.findViewById(R.id.refreshRate);
        refreshRate.setText("1m");
        return view;
    }





}
