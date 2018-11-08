package com.hfda.playwithwords;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Fragment_Introduction_Mode1 extends Fragment implements View.OnClickListener
{
    ImageButton backBtn;
    Button nextBtn;
    Introduction _container; //Activity bao hàm Fragment này

    public Fragment_Introduction_Mode1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _container=(Introduction)getActivity(); //lấy cái Activity đó
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_introduction_mode1, container, false);

        nextBtn=layout.findViewById(R.id.btn_next);
        backBtn=layout.findViewById(R.id.btn_back);

        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        return layout;
    }

    //khi người dùng nhấn button ở Fragment thì sẽ gửi thông điệp lên cho Activity ở trên
    @Override
    public void onClick(View v)
    {
        String action=null;

        if(v.getId() == nextBtn.getId())
        {
            action="NEXT";
        }
        if(v.getId() == backBtn.getId())
        {
            action="BACK";
        }
        _container.Action(action); //gửi lên cho Activity ở trên
    }

}
