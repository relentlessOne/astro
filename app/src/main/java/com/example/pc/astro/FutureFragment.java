package com.example.pc.astro;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;





public class FutureFragment extends Fragment {

    public LinearLayout linlay;




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.future,container,false);
        linlay = (LinearLayout) view.findViewById(R.id.linlay);



        return view;
    }



}

