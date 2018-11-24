package com.hfda.playwithwords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LogoutActivity extends AppCompatActivity
{
    TextView tvGoodBye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        tvGoodBye=findViewById(R.id.tvGoodBye);
        tvGoodBye.setText("See you soon, my friend");
    }
}
