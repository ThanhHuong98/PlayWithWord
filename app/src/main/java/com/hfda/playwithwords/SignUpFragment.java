package com.hfda.playwithwords;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {
    EditText txtUserName;
    EditText txtPassWords;
    EditText txtPassWordsConfirm;
    Button btnSignUp1;
   SignInSignUpActivity _container;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        txtUserName = v.findViewById(R.id.txtUserName);
        txtPassWords = v.findViewById(R.id.txtPassword);
        txtPassWordsConfirm = v.findViewById(R.id.txtConfirmPassword);
        btnSignUp1 = v.findViewById(R.id.btnSignUp1);
        btnSignUp1.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnSignUp1.getId()) {
            //intent to FlashActivity
            //txtUserName.setError("Invalid username");

            //txtUserName.setTextColor(getResources().getColor(R.color.colorAccent));
            if (validate())
            {
                //btnSignUp1.setEnabled(false);
                Intent intent = new Intent(getContext(),Menu.class);
                startActivity(intent);
            }
        }

    }
//Kiểm tra thỏa mãn quy định đăng ký
    public boolean validate() {
        boolean valid = true;
        String username = txtUserName.getText().toString();
        //String email = _emailText.getText().toString();
        String password = txtPassWords.getText().toString();
        String confirmPass = txtPassWordsConfirm.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            txtUserName.setError("at least 3 characters");
            txtUserName.requestFocus();
            valid = false;
        } else {
            txtUserName.setError(null);
        }


        if (password.isEmpty()||password.length()<4||password.length()>10) {
            txtPassWords.setError("between 4 and 10 alphanumeric characters");
            txtPassWords.requestFocus();
            if (username.isEmpty() || username.length() < 3) {
                txtUserName.requestFocus();
            }
            valid = false;
        } else {
            txtPassWords.setError(null);
        }
        if (!confirmPass.equals(password)) {
            txtPassWordsConfirm.setError("The confirm password does not match");
            txtPassWordsConfirm.requestFocus();
            valid = false;
        } else {
            txtPassWordsConfirm.setError(null);
        }
        return valid;
    }
}

