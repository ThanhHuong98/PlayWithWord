package com.hfda.playwithwords;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.os.Build.ID;
import static android.provider.Contacts.SettingsColumns.KEY;
import static java.sql.Types.INTEGER;
import static java.text.Collator.PRIMARY;

public class MyDatabase extends SQLiteOpenHelper
{
    private static final String DB_NAME="Database";
    private static final int DB_VERSION=1;
    MyDatabase(Context context)
    {
        //Khởi tạo database
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Tạo bảng chứa dữ liệu bộ câu hỏi
        db.execSQL("CREATE TABLE DATA ("
                +"ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"ENGLISH_TEXT TEXT, "
                +"TRANSCRIPTION TEXT, "
                +"VN_TEXT TEXT, "
                +"DESCRIPTION TEXT, "
                +"IMAGE INTEGER, "
                +"AUDIO INTEGER);");

        //Tạo bảng chứa thông tin user
        db.execSQL("CREATE TABLE USER_INFO ("
                +"ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"USERNAME TEXT, "
                +"PASSWORD TEXT, "
                +"TOTAL_SCORE INTEGER);");

        //Thêm dữ liệu vào bộ câu hỏi

    }

    private void InsertData(SQLiteDatabase db, String engText, String transcription, String vnText, String discription, int imageId, int audioId)
    {
        ContentValues dataValues = new ContentValues();
        dataValues.put("ENGLISH_TEXT", engText);
        dataValues.put("TRANSCRIPTION", transcription);
        dataValues.put("VN_TEXT", vnText);
        dataValues.put("DESCRIPTION", discription);
        dataValues.put("IMAGE", imageId);
        dataValues.put("AUDIO", audioId);

        db.insert("DATA", null, dataValues);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //ứng dụng của mình chưa cần update nên hàm này để trống
    }
}
