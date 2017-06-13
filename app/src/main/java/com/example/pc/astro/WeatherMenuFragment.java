package com.example.pc.astro;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;


public class WeatherMenuFragment extends Fragment {
    public Button refresh;
    public Button city;
    public Button fav;


    WeatherMenuListener WeatherMenuCommander;

    public interface WeatherMenuListener{
        public void refreshWeatherClick();
        public void cityWeatherClick();
        public void favWeatherClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            WeatherMenuCommander = (WeatherMenuFragment.WeatherMenuListener) context;
        } catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_menu,container,false);
        fav = (Button) view.findViewById(R.id.fav);
        refresh = (Button) view.findViewById(R.id.refresh);
        city = (Button) view.findViewById(R.id.city);


        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WeatherMenuCommander.refreshWeatherClick();
            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WeatherMenuCommander.cityWeatherClick();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WeatherMenuCommander.refreshWeatherClick();
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WeatherMenuCommander.favWeatherClick();
            }
        });

        return view;
    }








}
