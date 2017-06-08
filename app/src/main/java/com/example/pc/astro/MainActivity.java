package com.example.pc.astro;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;


import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements ButtonFragment.ButtonFragmentClicksListener {

    TimeLocalizationFragment timeLocalization;
    SunFragment sun;
    MoonFragment moon;

    String m_Text = "";
    String m_Text1 = "";
    double lat = 51.759248;
    double lng = 19.455983;
    long time = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadElements();
        startClock();
        refreshSunMoonData();
    }

    private void startClock() {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                java.util.Date noteTS = Calendar.getInstance().getTime();
                                String time = "hh:mm:ss";
                                timeLocalization.currentTime.setText(DateFormat.format(time, noteTS));

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private void loadElements() {
        timeLocalization = (TimeLocalizationFragment) getSupportFragmentManager().findFragmentById(R.id.timeloc);
        timeLocalization.localization.setText(lat + " " + lng);
        sun = (SunFragment) getSupportFragmentManager().findFragmentById(R.id.sun);
        moon = (MoonFragment) getSupportFragmentManager().findFragmentById(R.id.moon);
    }


    private void refreshSunMoonData() {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(time);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sun.loadSunInfo();
                                moon.loadMoonInfo();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }




    public void validateInputRefresh() {

        try {
            int newTime = Integer.parseInt(m_Text);
            time = newTime * 1000;
            timeLocalization.refreshRate.setText(m_Text+"m");
            Toast.makeText(this, "Czas został zmieniony",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Podana wartość nie jest liczbą całkowitą",
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void refreshClick() {
        m_Text = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input).setMessage("Odświeżanie w minutach");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                validateInputRefresh();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void validateInputLatLng(){
        try {
            Double newLat = Double.parseDouble(m_Text);
            Double newLng = Double.parseDouble(m_Text1);
            boolean valid = true;
            if(newLat > 90.0 || newLat < -90.0){
                valid = false;
            }

            if(newLat > 180.0 || newLat < -180.0){
                valid = false;
            }

            if(valid){
                sun.setLatLng(newLat, newLng);
                moon.setLatLng(newLat, newLng);

                sun.loadSunInfo();
                moon.loadMoonInfo();

                lat = newLat;
                lng = newLng;

                timeLocalization.localization.setText(lat + " " + lng);

                Toast.makeText(this, "Lokalizacja została zmieniona",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Dozowlony zakres dla LAT -90 90 LNG -180 180",
                        Toast.LENGTH_LONG).show();
            }




        } catch (Exception e) {
            Toast.makeText(this, "Podana wartość nie jest liczbą zmiennoprzecinkową",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void latlngClick() {
        m_Text = "";
        m_Text1 = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        final EditText input1 = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);



        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(input);
        ll.addView(input1);
        builder.setView(ll).setMessage("Lokalizacja LAT LNG");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                m_Text1 = input1.getText().toString();
                validateInputLatLng();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
