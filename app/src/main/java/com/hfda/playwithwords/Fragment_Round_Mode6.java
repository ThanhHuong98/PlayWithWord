package com.hfda.playwithwords;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.app.Activity.RESULT_OK;


public class Fragment_Round_Mode6 extends Fragment implements fromContainerToFrag, View.OnClickListener {
    private final int REQ_CODE_SPEECH_INPUT = 10;
    private TextToSpeech txts;
    ImageView button;
    Context context;
    Round _container;
    ProgressBar myProgressBar;
    static int countQuestion=0;
    private int points=0;
    String realAnswer="hello";
    Handler handler;
    Intent intent;
    int hint=5;
    ImageButton btnHint;
    TextView textViewNumberHint;
    TextView textViewPoint;
    TextView textViewQuestion;
    ImageView  imWave;
    String strOfSpeech;
    TextView textViewRound;
    int index=1;
    AtomicBoolean ab=new AtomicBoolean(false);
    private ArrayList<Integer> idArray = new ArrayList<>();
    @Override
    public void onClick(View v) {
        // takeTextToSpeech();
    }
    @Override
    public void InfoToHandle(String mess, String roundOfMode, Object newQuestion, Object answer, String transcription, String[] answerInBtn) {
        if(mess.equals("NEW"))
        {
            index++;
            btnHint.setEnabled(true);
            updateContent();

            textViewRound.setText(roundOfMode+ "/20");
            textViewPoint.setText(points+"");
            textViewQuestion.setText(realAnswer);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_round_mode6, container, false);
        myProgressBar = (layout).findViewById(R.id.myProgressBar);
        textViewRound = (layout).findViewById(R.id.textViewRound);
        btnHint=(layout).findViewById(R.id.btnHint);
        textViewNumberHint=(layout).findViewById(R.id.textViewNumberHint);
        textViewPoint=(layout).findViewById(R.id.textViewPoint);
        textViewQuestion=layout.findViewById(R.id.textViewQuestion);
        imWave=layout.findViewById(R.id.ic_wave);
        final RippleBackground rippleBackground=(layout).findViewById(R.id.content);
        button=layout.findViewById(R.id.centerImage);
        context=getActivity().getApplicationContext();

        // handler=new Handler();
        _container = (Round)getActivity();
        _container.Action("REFRESH");
        txts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.e("TTS", "TextToSpeech.OnInitListener.onInit...");
            }
        });
        btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hint--;
                String notification="";
                String strHint=hint+"";
                if(hint>=0)
                {
                    txts.speak(realAnswer,TextToSpeech.QUEUE_FLUSH, null);
                    textViewNumberHint.setText("Hint: "+strHint+""+"/5");
                    notification="Decrease the number of Hint...";
                }
                else{
                    notification="You're out of hint..... ";
                }
                Toast.makeText(context, notification, Toast.LENGTH_SHORT).show();
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN) {
                    button.setSelected(true);
                    imWave.setVisibility(View.INVISIBLE);
                    rippleBackground.startRippleAnimation();

                    getSpeechInput();
                    return true;
                } else if(event.getAction()==MotionEvent.ACTION_UP){
                    imWave.setVisibility(View.VISIBLE);

                    button.setSelected(false);
                    rippleBackground.stopRippleAnimation();
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_container);

//                    if(resultStringArrayList.get(0).equalsIgnoreCase(realAnswer)){
//                        alertDialogBuilder.setMessage("Correct");
//                        countQuestion++;
//                        if(countQuestion>20){
//                            countQuestion--;
//                        }
//                        textViewRound.setText(countQuestion+"/20");
//                        myProgressBar.setProgress(countQuestion*100/20);

//                    }
//                    else{
//                        alertDialogBuilder.setMessage("Failure");
//                    }
//
//                    count++;
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//                    //_container.Action("REFRESH");
                    return true;
                }

                return false;
            }
        });

        return layout;
    }

    public void updateContent(){
        try {
            SQLiteOpenHelper myDatabase = new MyDatabase(_container.getApplicationContext());
            SQLiteDatabase db = myDatabase.getReadableDatabase();
            int number_rows=0;
            Cursor cursor = db.query("DATA",
                    new String[]{"COUNT(ID) AS NUM_ROW"},
                    null, null, null, null, null); //truy vấn số lượng dòng

            if(cursor.moveToFirst())//di chuyển con trỏ lên dòng đầu tiên của bảng kết quả
            {
                number_rows = cursor.getInt(0); //lấy dữ liệu ở cột thứ 0, kiểu int
            }

            Random rd=new Random();
            //  int randRow = 1+(int)Math.random()*(number_rows-1); //random ra 1 dòng dữ liệu
            index=rd.nextInt(number_rows)+1;
            while(!idArray.isEmpty() && idArray.indexOf(index)!=-1)
            {
                index=rd.nextInt(number_rows)+1;
                //  randRow = 1+(int)Math.random()*(number_rows-1); //phải random lại dòng mới nếu random ra trùng với những câu đã lấy trước đó
            }
            idArray.add(index); //nếu không trùng thì thêm vào ArrayList

            cursor = db.query("MODE5",
                    null,
                    "ID = ?", new String[]{String.valueOf(index)}, null, null, null); //truy vấn số lượng dòng

            if (cursor.moveToFirst())//di chuyển con trỏ lên dòng đầu tiên của bảng kết quả
            {
                //textViewQuestion.setText(cursor.getString(2));
                realAnswer=cursor.getString(2);
            }
            cursor.close();
            db.close();
        }catch(SQLException ex) {
            Log.e("Error", "An error occured when trying to access database!");
        }

    }

    private double compareString(String strOfSpeech,String realAnswer){

        strOfSpeech= strOfSpeech.toLowerCase();
        realAnswer=realAnswer.toLowerCase();
        HashSet<String> set = new HashSet<String>();
        String[] splitStrOfSpeed = strOfSpeech.split(" ");
        for(int i=0;i<splitStrOfSpeed.length;i++){
            set.add(splitStrOfSpeed[i]);
        }
        int countCorrect=0;
        String[] splitRealAnswer = realAnswer.split(" ");
        for(int i=0;i<splitRealAnswer.length;i++){
            if(set.contains(splitRealAnswer[i])){
                countCorrect++;
            }
        }

        return countCorrect*1.0/splitRealAnswer.length;
    }
//    private void takeTextToSpeech(){
//        Intent checkIntent = new Intent();
//        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//        startActivityForResult(checkIntent, 11);
//    }

    private void getSpeechInput() {

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, 10);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(context, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    strOfSpeech=result.get(0);
                    double sim=compareString(strOfSpeech,realAnswer)*100;
                    int k=(int)Math.round(sim);
                    String nof="You only read exactly "+k+"%";
                    Toast.makeText(context,nof,Toast.LENGTH_LONG).show();
                    if(k>=60){
                            points+=20;
                        _container.Action("RIGHT");
                    }
                    else{
                        _container.Action("WRONG");
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            _container.Action("REFRESH");
                        }
                    }, 1000);
                }
                break;

        }
    }

}
