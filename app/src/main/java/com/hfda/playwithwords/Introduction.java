package com.hfda.playwithwords;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;
/*Activity chứa Fragmen_Introduction_mode1*/

public class Introduction extends AppCompatActivity implements fromFragToContainer
{
    public static final String CHECKMUSIC = "music";

    public static String MODE = "mode";
    Fragment fragment;
    int currentMode;
    boolean sound =true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        Intent intent = getIntent();
        currentMode = Integer.parseInt(intent.getStringExtra(MODE));
        //Intent intent = getIntent();
        switch(currentMode)
        {
            case 1:
                fragment = new Fragment_Introduction_Mode1();
                break;
            case 2:
                fragment = new Fragment_Introduction_Mode2();
                break;
            case 3:
                fragment = new Fragment_Introduction_Mode3();
                break;
            case 4:
                fragment = new Fragment_Introduction_Mode4();
                break;
            case 5:
                fragment = new Fragment_Introduction_Mode5();
                break;
            case 6:
                fragment = new Fragment_Introduction_Mode6();
                break;
        }
        //Create a new Frgment_Round_Modex and Show it
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_intro, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent intentback=new Intent(this, Menu.class);
        if(sound==false) {

            intentback.putExtra(CHECKMUSIC, "");
        }
        else
        {
            intentback.putExtra(CHECKMUSIC, "SOUND");
        }
        startActivity(intentback);
    }

    //cái interface để nhận thông tin của cái button được click dưới Fragment rồi gửi lên cho Activity
    @Override
    public void Action(String action) //thông điệp hành động mà ở dưới Fragment gửi lên
    {
        Intent intent=null;

        if(action.equals("MUTE_SOUND")){
            sound=false;
        }
        if(action.equals("NEXT")) //nghĩa là người dùng nhấn "Click here to play"
        {
            intent = new Intent(Introduction.this, Round.class); //neu nhan nut Next thi chuyen intent qua Round
            intent.putExtra(Round.MODE, currentMode + ""); //de xac dinh duoc mode nao dang hien hanh
        }
        if(action.equals("BACK")) //nghĩa là người dùng nhấn icon back
        {


            intent = new Intent(this, Menu.class); //neu nhan nut Back thi chuyen intent ve Menu
            if(sound==false) {

                intent.putExtra(CHECKMUSIC, "");
            }
            else
            {
                intent.putExtra(CHECKMUSIC, "SOUND");
            }
        }
        startActivity(intent);
        finish();
    }
}
