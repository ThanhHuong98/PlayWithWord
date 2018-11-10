package com.hfda.playwithwords;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
/*Chua Scroll View la btn 6 mode choi....*/
public class FragmentHome extends Fragment implements View.OnClickListener
{
ImageButton btnMode1, btnMode2,btnMode3,btnMode4,btnMode5,btnMode6;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.home_fragment, container, false);
       btnMode1=v.findViewById(R.id.btnMode1);
        btnMode2=v.findViewById(R.id.btnMode2);
        btnMode3=v.findViewById(R.id.btnMode3);
        btnMode4=v.findViewById(R.id.btnMode4);
        btnMode5=v.findViewById(R.id.btnMode5);
        btnMode6=v.findViewById(R.id.btnMode6);

        btnMode1.setOnClickListener(this);
        btnMode2.setOnClickListener(this);
        btnMode3.setOnClickListener(this);
        btnMode4.setOnClickListener(this);
        btnMode5.setOnClickListener(this);
        btnMode6.setOnClickListener(this);

        //btnMode1.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v)
    {
        String mode = null;
       if (v.getId() == btnMode1.getId())
       {
            mode = "1";

        }
        if (v.getId() == btnMode2.getId())
        {
            mode = "2";
        }
        if (v.getId() == btnMode3.getId())
        {
            mode = "3";
        }
        if (v.getId() == btnMode4.getId())
        {
            mode = "4";
        }

        if (v.getId() == btnMode5.getId())
        {
            mode = "5";
        }
        if (v.getId() == btnMode6.getId())
        {
            mode = "6";
        }
        Intent intent = new Intent(getActivity(), Introduction.class);
        intent.putExtra(Introduction.MODE, mode);
        startActivity(intent);
    }
}
