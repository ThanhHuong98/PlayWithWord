package com.hfda.playwithwords;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserLogedIn extends SQLiteOpenHelper
{
    private static final  String DB_NAME = "User Loged In";
    private static final int DB_VERSION = 1;
    UserLogedIn(Context context)
    {
        super(context, DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE USER ("
                + "USER_NAME TEXT,"
                + "PASSWORD TEXT,"
                + "USER_KEY INT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
