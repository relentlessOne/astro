package com.example.pc.astro;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;




public class ButtonFragment extends Fragment {
    public Button refresh;
    public Button latlng;

    ButtonFragmentClicksListener buttonsClicksCommander;

    public interface ButtonFragmentClicksListener{
        public void refreshClick();
        public void latlngClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            buttonsClicksCommander = (ButtonFragmentClicksListener) context;
        } catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_buttons,container,false);
        refresh = (Button) view.findViewById(R.id.refresh);
        latlng = (Button) view.findViewById(R.id.latlng);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonsClicksCommander.refreshClick();
            }
        });

        latlng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonsClicksCommander.latlngClick();
            }
        });
        return view;
    }





}
