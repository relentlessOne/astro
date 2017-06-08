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


public class SunFragment extends Fragment {

    AstroCalculator.SunInfo sun;
    TextView sun1;
    TextView sun2;
    TextView sun3;
    double lat = 51.759248;
    double lng = 19.455983;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sun,container,false);
        sun1 = (TextView) view.findViewById(R.id.sun1);
        sun2 = (TextView) view.findViewById(R.id.sun2);
        sun3 = (TextView) view.findViewById(R.id.sun3);
        loadSunInfo();
        return view;
    }

    public void loadSunInfo(){
        sun = new AstroCalculator(new AstroDateTime(), new AstroCalculator.Location(lat,lng)).getSunInfo();
        sun1.setText("czas: "+sun.getSunrise().getHour()+":"+sun.getSunrise().getMinute()+" azymut:"+String.format( "%.2f", sun.getAzimuthRise() ));
        sun2.setText("czas: "+sun.getSunset().getHour()+":"+sun.getSunset().getMinute()+" azymut:"+String.format( "%.2f", sun.getAzimuthSet() ));
        sun3.setText("zmierzch: "+sun.getTwilightEvening().getHour()+":"+sun.getTwilightEvening().getMinute()+" świt: "+sun.getTwilightMorning().getHour()+":"+sun.getTwilightMorning().getMinute());
    }

    public void setLatLng(double la, double ln){
        lat = la;
        lng = ln;
    }

}
