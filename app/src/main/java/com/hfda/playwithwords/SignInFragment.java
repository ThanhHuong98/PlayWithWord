package com.hfda.playwithwords;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {
    EditText txtUsername1;
    EditText txtPassword1;
    Button btnSignIn1;
    ImageButton btnGoogle;
    ImageButton btnFB;
    SignInSigUpActivity _container;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        _container=(SignInSigUpActivity) getActivity();

        txtUsername1 = v.findViewById(R.id.txtUserName1);
        txtPassword1 = v.findViewById(R.id.txtPassword1);
        btnSignIn1 = v.findViewById(R.id.btnSignIn1);
        btnGoogle = v.findViewById(R.id.btnGoogle);
        btnFB = v.findViewById(R.id.btnFB);
        btnSignIn1.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnFB.setOnClickListener(this);
        ///Xu  ly

        return v;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnSignIn1.getId()) {
            if(validate())
            {
                /*Context context = getContext();
                Toast toast = Toast.makeText(context, "Nguyen Quang Huan", Toast.LENGTH_LONG);
                toast.show();*/
                Intent intent = new Intent(getContext(),Menu.class);
                startActivity(intent);
            }
        }
        else if(v.getId()==btnGoogle.getId()){
            _container.Action("GOOGLE");
        }
        else if(v.getId()==btnFB.getId()){
            _container.Action("FB");

        }
    }
//Nay là kiểm tra thông tin user với thông tin đã SignUp lần đầu tiên được save trên fireBase
    public boolean validate() {
        boolean valid = true;
        String username = txtUsername1.getText().toString();
        String password = txtPassword1.getText().toString();
        String nameUserFireBase="ntThanhHuong";
        String passwordFireBase ="123456";

        if (username.isEmpty() || (!username.equals(nameUserFireBase))) {
            txtUsername1.setError("Username incorrect, check again");
            txtUsername1.requestFocus();
            valid = false;
        } else {
            txtUsername1.setError(null);
        }
        if (password.isEmpty()||(!password.equals(passwordFireBase)) ){
            txtPassword1.setError("Your password incorrect, check again");
            txtPassword1.requestFocus();
            if(username.isEmpty() || username.length() < 3)
            {
                txtUsername1.requestFocus();
            }
        } else {
            txtPassword1.setError(null);
        }
        return valid;
    }

}
