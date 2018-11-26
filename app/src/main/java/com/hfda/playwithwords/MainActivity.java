package com.hfda.playwithwords;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
{
    private Handler myHandler;
    public static List<DataMode1234> mData= new ArrayList<>();
    public static List<User> mUser= new ArrayList<>();
    public static DatabaseReference myref;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    //mảng lưu các quyền cần truy cập
   // private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    protected static Context context;
    protected static String userName;
    //protected static int userKey; //thứ tự node của user trên firebase
    protected static long totalScore;
    protected static int Userrank; //vị trí của user trong bảng xếp hạng

    protected static boolean CheckLogedIn()
    {
            SQLiteOpenHelper UserDB = new UserLogedIn(context);
            SQLiteDatabase db = UserDB.getReadableDatabase();
            Cursor cursor = db.query("USER",
                    new String[]{"COUNT(USER_NAME)"}, null, null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getInt(0)==0)
                return false;
            else
            {
                cursor = db.query("USER",
                        new String[]{"USER_NAME"}, null, null, null, null, null);
                cursor.moveToFirst();
                userName = cursor.getString(0);
                return true;
            }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout layout = findViewById(R.id.layout);
        context = getApplicationContext();
        myref=FirebaseDatabase.getInstance().getReference();
        readData();
        readUserInfo();
        if(CheckLogedIn())
        {
            layout.setBackground(getResources().getDrawable(R.drawable.welcome));
        }
        else
        {
            layout.setBackground(getResources().getDrawable(R.drawable.poweron));
        }
/*----------------------*/
        //đòi quyển truy cập
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        } else {
            startNextActivity();
        }
    }
    private  void readData()
    {
        myref.child("DB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for(DataSnapshot dts: dataSnapshot.getChildren())
                {
                    DataMode1234 data=dts.getValue(DataMode1234.class);
                    mData.add(data);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    protected static void readUserInfo()
    {
        myref.child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for(DataSnapshot dts: dataSnapshot.getChildren()) {
                    User data=dts.getValue(User.class);
                    if(data!=null)
                        mUser.add(data);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    protected static int indexUser(String Name) //trả ra vị trí của userName trong List
    {
        for(int i = 0; i< mUser.size(); i++)
        {
            if(mUser.get(i).getName().equalsIgnoreCase(Name))
                return i;
        }
        return -1;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            checkPermissions();
        }
    }

    private void checkPermissions() {
        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length == 0) {
            startNextActivity();
        } else {
            ActivityCompat.requestPermissions(this,ungrantedPermissions,REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void startNextActivity(){

        myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //kiểm tra xem trong bảng database bên dưới đã có ai đăng nhập chưa, nếu chưa thì chuyển intent qua màn hình đăng nhập, có rồi thì chuyển
                //thẳng qua menu luôn
                    if(!CheckLogedIn())
                    {
                        Intent intent = new Intent(getApplicationContext(), SignInSignUpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), Menu.class);
                        startActivity(intent);
                        finish();
                    }
            }
        }, 2000);
    }

    private String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<String>();
        for (String permission : getRequiredPermissions()) {
            permissions.add(permission);
        }
        for (Iterator<String> i = permissions.iterator(); i.hasNext();) {
            String permission = i.next();
            if (ActivityCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(MainActivity.class.getSimpleName(),
                        "Permission: " + permission + " already granted.");
                i.remove();
            } else {
                Log.d(MainActivity.class.getSimpleName(),
                        "Permission: " + permission + " not yet granted.");
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    public String[] getRequiredPermissions() {
        String[] permissions = null;
        try {
            permissions = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }

    /* public static boolean verifyStoragePermissions(Activity activity) {
       // Check if we have write permission
       int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

       if (permission != PackageManager.PERMISSION_GRANTED) {
           // We don't have permission so prompt the user
           ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
       }
       return true;
   }*/
    @Override
    public void onStop()
    {
        super.onStop();
    }
}
