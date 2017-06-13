package com.example.pc.astro;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements ButtonFragment.ButtonFragmentClicksListener, WeatherMenuFragment.WeatherMenuListener {

    TimeLocalizationFragment timeLocalization;
    SunFragment sun;
    MoonFragment moon;
    WeatherMenuFragment wmf;
    CityInfoFragment cif;
    AdditionalInfoFragment aif;
    FutureFragment ff;
    Context context;
    boolean lastModal;
    boolean allowInit;
    boolean isOnline = false;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/astroData";
    String m_Text = "";
    String m_Text1 = "";
    String choosenCity ;
    String choosenWOEID;
    double lat = 51.759248;
    double lng = 19.455983;
    long time = 1000;
    JSONArray favJSON;
    JSONArray forecast;
    List<String> forecastList;

    File rootDir;
    File favXML;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootDir = new File(path);




        context = this;
        allowInit = true;
        setContentView(R.layout.main);
        loadElements();
        startClock();
        refreshSunMoonData();

        checkNetConnection();
    }

    private void checkNetConnection(){
        choosenCity = "Lodz";
        choosenWOEID = "505120";
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(isConnected()){
                                    if(!lastModal || allowInit){
                                        Toast.makeText(context, "Connected to web",
                                                Toast.LENGTH_LONG).show();

                                        loadFav();
                                        isOnline = true;

                                        fetchData(choosenWOEID);

                                        lastModal = true;
                                        allowInit = false;
                                    }
                                }else {
                                    if(lastModal || allowInit){
//                                        Toast.makeText(context, "No internet connection",
//                                                Toast.LENGTH_LONG).show();

                                        fetchDataFromLocal(choosenWOEID);
                                        isOnline = false;
                                        lastModal = false;
                                        allowInit = false;
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }



    private void loadFav(){

        checkIfFileExists("fav.json");
     //   expandJson("fav.json","xzzzx","xx");



           // overwriteFile("fav.json","asdasdasd");
            //expandJson("fav.json","Warszawa","213123");
           // wmf.cityName.setText(readFromFile("fav.json"));



    }


    private void checkIfFileExists(String filename){
        try {
            FileInputStream fin = openFileInput(filename);

        } catch (FileNotFoundException e) {
            FileOutputStream fos = null;
            try {
                JSONArray initialFav = new JSONArray();
                JSONObject tmp = new JSONObject();
                tmp.put("Lodz","505120");
                initialFav.put(tmp);
                fos = openFileOutput(filename,MODE_PRIVATE);
                fos.write(initialFav.toString().getBytes());
                fos.close();

            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();

        }
    }

    private void expandJson(String filename,String key, String value){
        try {
            JSONArray jsonarr = new JSONArray(readFromFile(filename));
            JSONObject json = new JSONObject();
            json.put(key,value);
            jsonarr.put(json);
            overwriteFile(filename,jsonarr.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void overwriteFile(String filename,String msg){
        deleteFile(filename);
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename,MODE_PRIVATE);
            fos.write(msg.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String readFromFileLocal(String filename){
        try {
            String msg;
            FileInputStream fin = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader bis = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            while((msg = bis.readLine())!=null){
                sb.append(msg + '\n');
            }

            return sb.toString();
        } catch (Exception e) {
            Toast.makeText(this, filename,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return "noFile";

        }
    }

    private String readFromFile(String filename){
        try {
            String msg;
            FileInputStream fin = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader bis = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            while((msg = bis.readLine())!=null){
                sb.append(msg + '\n');
            }
            favJSON = new JSONArray(sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "noFile";

        }
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
                                cif.time.setText(DateFormat.format(time, noteTS));

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
        wmf = (WeatherMenuFragment) getSupportFragmentManager().findFragmentById(R.id.weatherMenu);
        cif = (CityInfoFragment) getSupportFragmentManager().findFragmentById(R.id.cityInfo);
        aif = (AdditionalInfoFragment)  getSupportFragmentManager().findFragmentById(R.id.addInfo);
        ff = (FutureFragment)  getSupportFragmentManager().findFragmentById(R.id.ff);



    }

    private void fetchDataFromLocal(String woeid){
        cif.name.setText(choosenCity);
        try {


            JSONObject res = new JSONObject(readFromFileLocal(woeid+".json"));



            JSONObject query = res.getJSONObject("query");
            JSONObject result = query.getJSONObject("results");
            JSONObject channel = result.getJSONObject("channel");

            JSONObject atmo = channel.getJSONObject("atmosphere");
            cif.pressure.setText(atmo.getString("pressure"));

            JSONObject wind = channel.getJSONObject("wind");

            aif.wind.setText("sila: "+wind.getString("speed")+" kierunek: "+wind.getString("direction"));

            JSONObject atmosphere = channel.getJSONObject("atmosphere");

            aif.hum.setText(atmosphere.getString("humidity"));

            aif.see.setText(atmosphere.getString("visibility"));

            JSONObject item = channel.getJSONObject("item");

            JSONObject condition = item.getJSONObject("condition");
            cif.temp.setText(condition.getString("temp"));

            cif.lat.setText(item.getString("lat"));
            cif.lng.setText(item.getString("long"));

            forecast = item.getJSONArray("forecast");

            forecastList = new ArrayList<String>();
            JSONArray jArray = forecast;
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    try {
                        forecastList.add(jArray.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            String cityinfos = forecastList.get(0);
            ff.linlay.removeAllViews();
            int count = 0;
            for(String el : forecastList){
                if(count != 0){

                    JSONObject json = new JSONObject(el);



                    TextView date = new TextView(context);
                    date.setText(json.getString("date"));
                    date.setGravity(Gravity.CENTER);
                    date.setBackgroundColor(Color.DKGRAY);




                    TextView high = new TextView(context);
                    high.setText("high: "+json.getString("high"));
                    high.setGravity(Gravity.CENTER);

                    TextView low = new TextView(context);
                    low.setText("low: "+json.getString("low"));
                    low.setGravity(Gravity.CENTER);

                    ImageView img = new ImageView(context);
                    Picasso.with(context).load("http://l.yimg.com/a/i/us/we/52/"+json.getString("code")+".gif").into(img);

                    TextView text = new TextView(context);
                    text.setText(json.getString("text"));
                    text.setGravity(Gravity.CENTER);

                    ff.linlay.addView(date);
                    ff.linlay.addView(high);
                    ff.linlay.addView(low);
                    ff.linlay.addView(img);
                    ff.linlay.addView(text);
                }

                count++;

            }

            JSONObject cityinfosjson = new JSONObject(cityinfos);
            Picasso.with(context).load("http://l.yimg.com/a/i/us/we/52/"+cityinfosjson.getString("code")+".gif").into(cif.image);

            cif.info.setText(cityinfosjson.getString("text"));



        } catch (JSONException e) {


            e.printStackTrace();

        }

    }

    private void fetchData(final String woeid){
        cif.name.setText(choosenCity);


        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid="+woeid+"%20and%20u=%22c%22&format=json";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody == null) { /* empty response, alert something*/ return; }
                //success response, do something with it!
                String response = new String(responseBody);

                overwriteFile(woeid+".json",response);

                try {
                    JSONObject res = new JSONObject(response);
                    JSONObject query = res.getJSONObject("query");
                    JSONObject result = query.getJSONObject("results");
                    JSONObject channel = result.getJSONObject("channel");

                    JSONObject atmo = channel.getJSONObject("atmosphere");
                    cif.pressure.setText(atmo.getString("pressure"));

                    JSONObject wind = channel.getJSONObject("wind");

                    aif.wind.setText("sila: "+wind.getString("speed")+" kierunek: "+wind.getString("direction"));

                    JSONObject atmosphere = channel.getJSONObject("atmosphere");

                    aif.hum.setText(atmosphere.getString("humidity"));

                    aif.see.setText(atmosphere.getString("visibility"));

                    JSONObject item = channel.getJSONObject("item");

                    JSONObject condition = item.getJSONObject("condition");
                    cif.temp.setText(condition.getString("temp"));

                    cif.lat.setText(item.getString("lat"));
                    cif.lng.setText(item.getString("long"));

                    forecast = item.getJSONArray("forecast");

                    forecastList = new ArrayList<String>();
                    JSONArray jArray = forecast;
                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            try {
                                forecastList.add(jArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    String cityinfos = forecastList.get(0);
                    ff.linlay.removeAllViews();
                    int count = 0;
                    for(String el : forecastList){
                        if(count != 0){



                            JSONObject json = new JSONObject(el);



                            TextView date = new TextView(context);
                            date.setText(json.getString("date"));
                            date.setGravity(Gravity.CENTER);
                            date.setBackgroundColor(Color.DKGRAY);




                            TextView high = new TextView(context);
                            high.setText("high: "+json.getString("high"));
                            high.setGravity(Gravity.CENTER);

                            TextView low = new TextView(context);
                            low.setText("low: "+json.getString("low"));
                            low.setGravity(Gravity.CENTER);

                            ImageView img = new ImageView(context);
                            Picasso.with(context).load("http://l.yimg.com/a/i/us/we/52/"+json.getString("code")+".gif").into(img);

                            TextView text = new TextView(context);
                            text.setText(json.getString("text"));
                            text.setGravity(Gravity.CENTER);

                            ff.linlay.addView(date);
                            ff.linlay.addView(high);
                            ff.linlay.addView(low);
                            ff.linlay.addView(img);
                            ff.linlay.addView(text);
                        }

                        count++;

                    }

                    JSONObject cityinfosjson = new JSONObject(cityinfos);
                    Picasso.with(context).load("http://l.yimg.com/a/i/us/we/52/"+cityinfosjson.getString("code")+".gif").into(cif.image);

                    cif.info.setText(cityinfosjson.getString("text"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody == null) { /* empty response, alert something*/ return; }
                //error response, do something with it!
                String response = new String(responseBody);
            }
        });

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
        input.setInputType(InputType.TYPE_CLASS_TEXT );
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
        input.setInputType(InputType.TYPE_CLASS_TEXT );

        final EditText input1 = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );



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

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void refreshWeatherClick() {
        if(isOnline){
            fetchData(choosenWOEID);
        } else{
            fetchDataFromLocal(choosenWOEID);
        }

    }

    private void createLocalWeatherFile( String woeid){
        final String cpy = woeid;
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid="+woeid+"%20and%20u=%22c%22&format=json";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                overwriteFile(cpy+".json",response);


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error)
            {

            }
        });
    }

    private boolean validateInputNewCity(String msg){
        final String msgCpy = msg;
        List<String> listdata = new ArrayList<String>();
        JSONArray jArray = favJSON;
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                try {
                    listdata.add(jArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        for(String el : listdata){
            try {
                JSONObject tmp = new JSONObject(el);
                Iterator<?> keys = tmp.keys();
                final String key = (String)keys.next();
                if(key.equals(msg)){
                    Toast.makeText(this, "Istnieje miasto "+msg+" w ulubionych",
                            Toast.LENGTH_LONG).show();
                    return  false;
                } else {

                    String url = "https://query.yahooapis.com/v1/public/yql?q=select * from geo.places(1) where text=\""+msg+"\"&format=json";
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (responseBody == null) { /* empty response, alert something*/ return; }
                            //success response, do something with it!
                            String response = new String(responseBody);

                            try {
                                JSONObject resjson = new JSONObject(response);

                                JSONObject query = resjson.getJSONObject("query");

                                String count = query.getString("count");
                                if(!count.equals("0")){
                                    JSONObject results = query.getJSONObject("results");
                                    JSONObject place = results.getJSONObject("place");
                                    String placename = place.getString("name");
                                    String woeid = place.getString("woeid");
                                    expandJson("fav.json",placename,woeid);


                                    createLocalWeatherFile(woeid);


                                    Toast.makeText(context, placename+" zostal dodany",Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "Nie znaleziono miasta "+msgCpy,Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            if (responseBody == null) { /* empty response, alert something*/ return; }
                            //error response, do something with it!
                            String response = new String(responseBody);
                        }
                    });

                    return true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return true;
    };

    @Override
    public void cityWeatherClick() {
        m_Text = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input).setMessage("Nazwa miasta");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                validateInputNewCity(m_Text);
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

    @Override
    public void favWeatherClick() {
        readFromFile("fav.json");
        List<String> listdata = new ArrayList<String>();
        JSONArray jArray = favJSON;
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                try {
                    listdata.add(jArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);


        for(String el : listdata){
            Button btnTag = new Button(this);

            try {
                final JSONObject json = new JSONObject(el);
                Iterator<?> keys = json.keys();
                final String key = (String)keys.next();
                btnTag.setText(key);
                btnTag.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                       choosenCity = key;
                        try {
                            choosenWOEID = json.getString(key);

                            if(isOnline){
                                fetchData(choosenWOEID);
                            } else {
                                fetchDataFromLocal(choosenWOEID);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ll.addView(btnTag);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        ScrollView sv = new ScrollView(this);
        sv.addView(ll);
        builder.setView(sv);



        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}
