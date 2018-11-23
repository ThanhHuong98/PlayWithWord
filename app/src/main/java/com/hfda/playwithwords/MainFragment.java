package com.hfda.playwithwords;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    Button btnSignIn;
    Button btnSignUp;
    SignIn_SignUpActivity _container;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_main, container, false);
        _container=(SignIn_SignUpActivity) getActivity();
        ///Xu  ly
        btnSignIn=(layout).findViewById(R.id.btnSignIn);
        btnSignUp=(layout).findViewById(R.id.btnSignUp);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);


        return layout;
    }

    @Override
    public void onClick(View v)
    {

        if(v.getId()==btnSignIn.getId())
        {
            _container.Action("SIGN IN");
        }else if(v.getId()==btnSignUp.getId())
        {
            _container.Action("SIGN UP");
        }
    }
}
