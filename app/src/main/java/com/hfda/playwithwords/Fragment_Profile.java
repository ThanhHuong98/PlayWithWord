package com.hfda.playwithwords;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Profile extends Fragment implements View.OnClickListener
{
    RatingBar mRatingBar;
    TextView tvRatingScale;
    EditText txtFeedBack;
    Button btnSendFeedBack;
    public Fragment_Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment__profile, container, false);

        return v;
    }

    @Override
    public void onClick(View v)
    {

    }
}
