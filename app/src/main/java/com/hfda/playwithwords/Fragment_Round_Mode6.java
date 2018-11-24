package com.hfda.playwithwords;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.List;
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
    List<Mode5> mangData=new ArrayList<>();
    TextView textViewQuestion;
    ImageView  imWave;
    String strOfSpeech;
    TextView textViewRound;
    int index=1;
    int[] dd=new int[30];
    AtomicBoolean ab=new AtomicBoolean(false);
    private ArrayList<Integer> idArray = new ArrayList<>();
    @Override
    public void onClick(View v) {
    }
    @Override
    public void InfoToHandle(String mess, String roundOfMode,String answer, String question,String Trans,String deFine,String[]answerBtn){
        if(mess.equals("NEW"))
        {
            index++;
            btnHint.setEnabled(true);
            updateContent();
            textViewRound.setText(roundOfMode+ "/20");
            textViewPoint.setText(points+"");

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

        for(int i=0;i<dd.length;i++) dd[i]=0;
        // handler=new Handler();

        _container = (Round)getActivity();
        checkPermission();
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

                String notification="";

                if(hint==1)
                {
                    if(Round.flag==0)
                    {
                        Round.HienThongBaoMuaHint(getContext());
                    }
                }
                if(hint>0)
                {
                    hint--;
                    String strHint=hint+"";
                    txts.speak(realAnswer,TextToSpeech.QUEUE_FLUSH, null);
                    textViewNumberHint.setText("Hint: "+strHint+""+"/5");
                    notification="Decrease the number of Hint...";
                }
                else{
                    final TextView tvHey;
                    final TextView tvNofitication;
                    final Button btnOkeBuy;
                    final Button btnCancelBuy;
                    final Button btnExitBuyHint;
                    final Dialog dialog=new Dialog(getContext());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_buy_hint);
                    tvHey=(TextView) dialog.findViewById(R.id.tvHey);
                    tvNofitication=(TextView)dialog.findViewById(R.id.tvNotification);
                    btnOkeBuy=(Button)dialog.findViewById(R.id.btnOkeBuyHint);
                    btnCancelBuy=(Button)dialog.findViewById(R.id.btnCancelBuyHint);
                    btnExitBuyHint=(Button)dialog.findViewById(R.id.btnExit);
                    btnCancelBuy.setVisibility(View.VISIBLE);
                    btnOkeBuy.setVisibility(View.VISIBLE);
                    btnExitBuyHint.setVisibility(View.INVISIBLE);
                    btnOkeBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(points>=7)
                            {
                                points -=7;
                                hint++;
                                String text = "Hint: " + hint + "/5";
                                String txtpoint = points + "";
                                textViewNumberHint.setText(text);
                                textViewPoint.setText(txtpoint);

                                dialog.dismiss();
                            }
                            else
                            {
                                tvHey.setText("Sorry!");
                                tvNofitication.setText("You don't have enough point to buy hint.");
                                btnExitBuyHint.setVisibility(View.VISIBLE);
                                btnCancelBuy.setVisibility(View.INVISIBLE);
                                btnOkeBuy.setVisibility(View.INVISIBLE);
                                btnExitBuyHint.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        btnExitBuyHint.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }
                    });
                    btnCancelBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    tvHey.setText("Hey,");
                    tvNofitication.setText("Just 7 points for 1 hint.\nDo you really want to buy more hint?");
                    dialog.show();
                }
                //Toast.makeText(context, notification, Toast.LENGTH_SHORT).show();
            }
        });

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
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

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN) {
                    button.setSelected(true);
                    imWave.setVisibility(View.INVISIBLE);
                    rippleBackground.startRippleAnimation();

                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    return true;
                } else if(event.getAction()==MotionEvent.ACTION_UP){
                    imWave.setVisibility(View.VISIBLE);
                    mSpeechRecognizer.stopListening();
                    button.setSelected(false);
                    rippleBackground.stopRippleAnimation();
                    return true;
                }

                return false;
            }
        });

        return layout;
    }

    public void updateContent(){
        DatabaseReference myref=FirebaseDatabase.getInstance().getReference();
        myref.child("DBmode5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mangData.clear();
                for (DataSnapshot dts : dataSnapshot.getChildren()) {
                    Mode5 mode5 = dts.getValue(Mode5.class);
                    mangData.add(mode5);
                }
                Random rd = new Random();
                int index = rd.nextInt(mangData.size());
                while(true)
                {
                    if(dd[index]==1)
                    {
                        index=rd.nextInt(mangData.size());
                    }
                    else
                    {
                        dd[index]=1;
                        break;
                    }
                }
                realAnswer = mangData.get(index).getTA();
                textViewQuestion.setText(realAnswer);
               // Toast.makeText(getContext(),realAnswer,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + context.getPackageName()));
                startActivity(intent);
                _container.finish();
            }
        }
    }
}
