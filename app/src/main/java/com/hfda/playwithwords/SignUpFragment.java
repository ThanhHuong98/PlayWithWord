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
        _container = (SignInSignUpActivity)getActivity();
        return v;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnSignUp1.getId())
        {
            if (validate())
            {
                //Thêm thông tin người dùng vào firebase
                MainActivity.mUser.add(new User(txtUserName.getText().toString(), txtPassWords.getText().toString(), 0));

                MainActivity.myref.child("UserInfo").child(String.valueOf(MainActivity.mUser.size())).child("name").setValue(txtUserName.getText().toString());
                MainActivity.myref.child("UserInfo").child(String.valueOf(MainActivity.mUser.size())).child("password").setValue(txtPassWords.getText().toString());
                MainActivity.myref.child("UserInfo").child(String.valueOf(MainActivity.mUser.size())).child("totalScore").setValue(0);

                //Hiện Dialog thông báo dang ky thanh cong, người dùng vào MenuScreen
                TextView tvContentSucces;
                Button btnGetStart;
                final Dialog dialog=new Dialog(getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_signin_signup_success);

                tvContentSucces=dialog.findViewById(R.id.tvContentSuccess);
                tvContentSucces.setText("You have already signed up!");
                btnGetStart=dialog.findViewById(R.id.btnGetStart);
                btnGetStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        _container.Action("SIGN IN");
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
                    value.put("USER_KEY", MainActivity.indexUser(username));
                    long insertToDb = db.insert("USER", null, value);
                    db.close();
                }catch(SQLiteException ex)
                {
                    Toast.makeText(_container, "Failed to connect to data base! You must log in again in the next time!", Toast.LENGTH_LONG);
                }*/
            }
        }

    }
//Kiểm tra thỏa mãn quy định đăng ký
    public boolean validate()
    {
        boolean valid = true;
        String username = txtUserName.getText().toString();
        //String email = _emailText.getText().toString();
        String password = txtPassWords.getText().toString();
        String confirmPass = txtPassWordsConfirm.getText().toString();

        //check xem user đã tồn tại trên csdl hay chưa
        if(MainActivity.indexUser(username)!=-1)
        {
            txtUserName.setError("User name has been existed!");
            txtUserName.requestFocus();
            valid = false;
        }
        //check xem user name đã đủ số ký tự quy định hay chưa
        else if (username.isEmpty() || username.length() < 3 || username.length() >8)
        {
            txtUserName.setError("User name must consists at least 3 characters and maximum character is 8!");
            txtUserName.requestFocus();
            valid = false;
        }
        else {
            txtUserName.setError(null);
        }

        //check xem password đã đủ từ 4-10 ký tự hay chưa
        if (password.isEmpty()||password.length()<4||password.length()>10) {
            txtPassWords.setError("Password must be between 4 and 10 alphanumeric characters!");
            txtPassWords.requestFocus();
            if (username.isEmpty() || username.length() < 3) {
                txtUserName.requestFocus();
            }
            valid = false;
        }
        //có bao gồm ít nhất 1 chữ cái hay không
        else if (!CheckPass(password))
        {
            txtPassWords.setError("Password must have at least one letter!");
            txtPassWords.requestFocus();
            if (username.isEmpty() || username.length() < 3) {
                txtUserName.requestFocus();
            }
            valid = false;
        }
        else {
            txtPassWords.setError(null);
        }

        if (!confirmPass.equals(password)) {
            txtPassWordsConfirm.setError("The confirming password does not match!");
            txtPassWordsConfirm.requestFocus();
            valid = false;
        }
        else {
            txtPassWordsConfirm.setError(null);
        }
        return valid;
    }


    private boolean CheckPass(String pass) //password phải gồm có ít nhất 1 chữ cái
    {
        for(int i=0; i< pass.length(); i++)
        {
            if(Character.isLetter(pass.charAt(i)))
                return true;
        }
        return false;
    }
}

