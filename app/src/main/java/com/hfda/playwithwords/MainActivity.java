package com.hfda.playwithwords;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnMode1, btnMode4, btnMode3;
    Button btnMode5;
    Button btnMode6;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    //mảng lưu các quyền cần truy cập
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMode1 = findViewById(R.id.btnMode1);
        btnMode3 = findViewById(R.id.btnMode3);
        btnMode4 = findViewById(R.id.btnMode4);
        btnMode5 = findViewById(R.id.btnMode5);
        btnMode6 = findViewById(R.id.btnMode6);
        btnMode1.setOnClickListener(this);
        btnMode3.setOnClickListener(this);
        btnMode4.setOnClickListener(this);
        btnMode5.setOnClickListener(this);
        btnMode6.setOnClickListener(this);
        //đòi quyển truy cập
        boolean canAccess = false;
        canAccess = verifyStoragePermissions(this);
        while (!canAccess) {
            Toast.makeText(this, "You must allow app access your storage to share your score!", Toast.LENGTH_LONG).show();
            canAccess = verifyStoragePermissions(this);
        }
    }

    @Override
    public void onClick(View v) {
        String mode = null;
        if (v.getId() == btnMode1.getId()) {
            mode = "1";
        }
        if (v.getId() == btnMode4.getId()) {
            mode = "4";
        }
        if (v.getId() == btnMode3.getId()) {
            mode = "3";
        }
        if (v.getId() == btnMode5.getId()) {
            mode = "5";
        }
        if (v.getId() == btnMode6.getId()) {
            mode = "6";
        }
        Intent intent = new Intent(this, Introduction.class);
        intent.putExtra(Introduction.MODE, mode);
        startActivity(intent);
    }

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
}
