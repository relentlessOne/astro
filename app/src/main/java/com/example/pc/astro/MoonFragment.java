package com.example.pc.astro;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;


public class MoonFragment extends Fragment {
    AstroCalculator.MoonInfo moon;
    TextView moon1;
    TextView moon2;
    TextView moon3;
    TextView moon4;
    double lat = 51.759248;
    double lng = 19.455983;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moon,container,false);
        moon1 = (TextView) view.findViewById(R.id.moon1);
        moon2 = (TextView) view.findViewById(R.id.moon2);
        moon3 = (TextView) view.findViewById(R.id.moon3);
        moon4 = (TextView) view.findViewById(R.id.moon4);
        loadMoonInfo();
        return view;
    }

    public void loadMoonInfo(){
        moon = new AstroCalculator(new AstroDateTime(), new AstroCalculator.Location(lat,lng)).getMoonInfo();
        moon1.setText("wschód: "+moon.getMoonrise().getHour()+":"+moon.getMoonrise().getMinute()+" zachód:"+moon.getMoonset().getHour()+":"+moon.getMoonset().getMinute());
        moon2.setText("nów: "+moon.getNextNewMoon().getDay()+"/"+moon.getNextNewMoon().getMonth()+"/"+moon.getNextNewMoon().getYear()+" pełnia:"+moon.getNextFullMoon().getDay()+"/"+moon.getNextFullMoon().getMonth()+"/"+moon.getNextFullMoon().getYear());
        moon3.setText(String.format( "%.2f", moon.getIllumination()*100 )+"%");
        moon4.setText(""+moon.getNextNewMoon().getDay());
    }
    public void setLatLng(double la, double ln){
        lat = la;
        lng = ln;
    }
}
