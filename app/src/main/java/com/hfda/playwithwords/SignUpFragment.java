package com.hfda.playwithwords;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
        if (v.getId() == btnSignUp1.getId())
        {
            if (validate())
            {
                //Hiện Dialog thông báo dang ky thanh cong, người dùng vào MenuScreen
                TextView tvContentSucces;
                Button btnGetStart;

                final Dialog dialog=new Dialog(getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_signin_signup_success);

                tvContentSucces=dialog.findViewById(R.id.tvContentSuccess);
                tvContentSucces.setText("You have already signed up.\nHave a fun time!");
                btnGetStart=dialog.findViewById(R.id.btnGetStart);
                btnGetStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),Menu.class);
                        startActivity(intent);
                    }
                });

                dialog.show();

                /*//nếu đăng ky tai khoan thành công thì add người dùng vào database bên dưới để lần sau k cần đn lại
                String username = txtUserName.getText().toString();
                String password = txtPassWords.getText().toString();
                try
                {
                    SQLiteOpenHelper UserDB = new UserLogedIn(_container);
                    SQLiteDatabase db = UserDB.getReadableDatabase();
                    ContentValues value = new ContentValues();
                    value.put("USER_NAME", username);
                    value.put("PASSWORD", password);
                    long insertToDb = db.insert("USER", null, value);
                    db.close();
                }catch(SQLiteException e)
                {
                    Toast.makeText(_container, "Failed to connect to data base! You must log in again in the next time!", Toast.LENGTH_LONG);
                }*/
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

