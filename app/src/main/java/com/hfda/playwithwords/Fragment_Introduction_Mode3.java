package com.hfda.playwithwords;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class Fragment_Introduction_Mode3 extends Fragment implements View.OnClickListener {

    ImageButton backBtn;
    Button nextBtn;
    Introduction _container;

    public Fragment_Introduction_Mode3() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        _container=(Introduction) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout layout =(RelativeLayout)inflater.inflate(R.layout.fragment_introduction_mode3, container, false);

        nextBtn=layout.findViewById(R.id.btnNext3);
        backBtn=layout.findViewById(R.id.btn_back);

        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        return layout;
        //return inflater.inflate(R.layout.fragment__introduction__mode3, container, false);

    }
    @Override
    public void onClick(View v)
    {
        String action=null;
        //View item = null; //cai item bi click
        if(v.getId() == nextBtn.getId())
        {
            //item = nextBtn;
            action="NEXT";

        }
        if(v.getId() == backBtn.getId())
        {
            //item = backBtn;
            action="BACK";
        }
        _container.Action(action);
    }


}

