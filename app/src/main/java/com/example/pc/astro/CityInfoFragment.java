package com.example.pc.astro;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;





public class CityInfoFragment extends Fragment {


    public TextView name;
    public TextView lat;
    public TextView lng;
    public TextView time;
    public TextView temp;
    public TextView pressure;
    public ImageView image;
    public TextView info;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_info,container,false);
        name = (TextView) view.findViewById(R.id.name);
        lat = (TextView) view.findViewById(R.id.lat);
        lng = (TextView) view.findViewById(R.id.lng);
        time = (TextView) view.findViewById(R.id.time);
        temp = (TextView) view.findViewById(R.id.temp);
        pressure = (TextView) view.findViewById(R.id.pressure);
        image = (ImageView) view.findViewById(R.id.image);
        info = (TextView) view.findViewById(R.id.info);

        return view;
    }



}
