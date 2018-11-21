package com.hfda.playwithwords;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private Button btnIntentMenu;
    private Handler myHandler;
    private Runnable runnable;
    private boolean canAccess = false;
    public static List<DataMode1234> mData= new ArrayList<>();
    DatabaseReference myref;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    //mảng lưu các quyền cần truy cập
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*----------------------*/

        //đòi quyển truy cập
        myref=FirebaseDatabase.getInstance().getReference();
        canAccess = verifyStoragePermissions(this);
        while (!canAccess) {
            Toast.makeText(this, "You must allow app access your storage to share your score!", Toast.LENGTH_LONG).show();
            canAccess = verifyStoragePermissions(this);
        }
            //Sau 3s thì sẽ tự động chuyển qua menu
            myHandler = new Handler();
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    while(!canAccess){}
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    startActivity(intent);
                }
            }, 3000);
        readData();

    }
    private  void readData()
    {
        myref.child("DB").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for(DataSnapshot dts: dataSnapshot.getChildren()) {
                    DataMode1234 data=dts.getValue(DataMode1234.class);
                    mData.add(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//    @Override
//    public void onClick(View v) {
//
//      if(v.getId()==btnIntentMenu.getId())
//      {
//          //Chuyen den Tab Menu..
//          Intent intent = new Intent(getApplicationContext(), Menu.class);
//          startActivity(intent);
//      }
//
//    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */

    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
        return true;
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }
}
