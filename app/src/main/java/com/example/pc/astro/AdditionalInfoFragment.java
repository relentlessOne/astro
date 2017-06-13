

package com.example.pc.astro;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;





public class AdditionalInfoFragment extends Fragment {


    public TextView wind;
    public TextView hum;
    public TextView see;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.additional_info,container,false);
        wind = (TextView) view.findViewById(R.id.wind);
        hum = (TextView) view.findViewById(R.id.hum);
        see = (TextView) view.findViewById(R.id.see);


        return view;
    }



}
