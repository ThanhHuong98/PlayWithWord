package com.hfda.playwithwords;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Result extends AppCompatActivity implements fromFragToContainer
{
    static final String FINAL_RESULT = "result";
    static final String TOTAL_SCORE = "score";
    static final String MODE = "mode";
    public static final String CHECKMUSIC = "music";

    Fragment_Result_Mode14 fragmentResultMode14;
    Fragment_Result_Mode3 fragmentResultMode3;
    Fragment_Result_Mode2 fragmentResultMode2;
    String finalResult;
    String totalScore;
    int mode;
    File imageFile;
    Context context;
    MediaPlayer mediaPlayer;
    boolean sound =true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mediaPlayer=MediaPlayer.create(this,R.raw.gaming);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Intent intent = getIntent();
        mode = Integer.parseInt(intent.getStringExtra(MODE));
        finalResult = intent.getStringExtra(FINAL_RESULT);
        totalScore = intent.getStringExtra(TOTAL_SCORE);
        switch(mode)
        {
            case 1: case 4:
                fragmentResultMode14 = new Fragment_Result_Mode14();
                ft.replace(R.id.fragment_result, fragmentResultMode14);
                break;
            case 2: case 5:
                fragmentResultMode2 = new Fragment_Result_Mode2();
                ft.replace(R.id.fragment_result,fragmentResultMode2);
                break;
            case 3: case 6:
                fragmentResultMode3 = new Fragment_Result_Mode3();
                ft.replace(R.id.fragment_result, fragmentResultMode3);
                break;
        }
        ft.commit();
        context = getApplicationContext();
    }

    @Override
    public void Action(String action)
    {
        if(action.equals("MUTE_SOUND")){
            sound=false;
        }
        if(action.equals("REFRESH"))
        {
            switch (mode)
            {
                case 1: case 4:
                    //Activity Round sẽ gửi bộ dữ liệu gồm câu hỏi và 4 đáp án mới xuống cho Fragment set lại
                    //thông qua phương thức InfoToHandle của Interface fromContainerToFrag
                fragmentResultMode14.InfoToHandle(totalScore, finalResult,"","","","",null);
                    break;
                case 2: case 5:
                fragmentResultMode2.InfoToHandle(totalScore, finalResult,"","","","",null);
                    break;
                case 3: case 6:
                fragmentResultMode3.InfoToHandle(totalScore, finalResult,"","","","",null);
                    break;
            }
        }
        if(action.equals("SHARE"))
        {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            Bitmap bitmap = takeScreenshot(rootView);
            saveBitmap(bitmap);
            shareIt();

            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
        }
        if(action.equals("FINISH"))
        {
            Intent intent = new Intent(this, Menu.class);
            if(sound==false) {

                intent.putExtra(CHECKMUSIC, "");
            }
            else
            {
                intent.putExtra(CHECKMUSIC, "SOUND");
            }
            startActivity(intent);
            finish();
        }

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
        finish();
    }

    public Bitmap takeScreenshot(View view)
    {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void saveBitmap(Bitmap bitmap)
    {
        String fileStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "ScreenshotMode" + mode + fileStamp + ".jpeg";

        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED))
        {
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                Log.e("GREC", e.getMessage(), e);
            } catch (IOException e) {
                Log.e("GREC", e.getMessage(), e);
            }
        }

    }

    private void shareIt()
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", imageFile);
        sharingIntent.setType("image/*");

        String shareBody = "My score is " + totalScore + ". Can you beat me? Download Play With Words now!";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My score");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Choose an app to share..."));
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        finish();
    }
    @Override
    public void onStop()
    {
        super.onStop();
        mediaPlayer.stop();
        finish();
    }
}
