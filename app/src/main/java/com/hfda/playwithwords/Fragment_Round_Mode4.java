package com.hfda.playwithwords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fragment_Round_Mode4 extends Fragment implements fromContainerToFrag, View.OnClickListener{
    ImageButton btnVolume;
    EditText editTextAnswer;
    Button btnDone;
    ImageButton btnHint;
    TextView textViewNumberHint;
    TextView textViewPoint;
    TextView textViewRound;
    ProgressBar myProgressBar;
    boolean run = true;
    int resID;
    Context context;
    Round _container;
    int[] dd=new int[30];
    int accum;
    int progressStep=1;
    Handler myHandler;
    Runnable runnable;

    int hint = 5;
    Round mainRoundMode4;
    SoundManager soundManager=new SoundManager();
    String realAnswer;
    int point=0;
    Character[] res;
    int i=0;
    String str;
    List<DataMode1234> mData=new ArrayList<>();
    String mQuestion;
    String mAnswer;

    public Fragment_Round_Mode4()
    {

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        mainRoundMode4=(Round) getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_round_mode4, container, false);
        _container = (Round)getActivity();
        btnVolume=(ImageButton) view.findViewById(R.id.btn_volume);
        editTextAnswer=(EditText)view.findViewById(R.id.editText_Answer);
        btnDone=(Button)view.findViewById(R.id.btn_done);
        btnHint=(ImageButton)view.findViewById(R.id.btnHint);
        textViewNumberHint=(TextView)view.findViewById(R.id.textViewNumberHint);
        textViewPoint=(TextView)view.findViewById(R.id.textViewPoint);
        textViewRound=(TextView)view.findViewById(R.id.textViewRound);

        myProgressBar = (ProgressBar)view.findViewById(R.id.progressbar);
        context=getActivity().getApplicationContext();


        //Yêu cầu activity Round Mode4 gửi data dữ liệu về, ngay từ round 1
        mainRoundMode4.Action("REFRESH");
        btnDone.setOnClickListener(this);
        btnVolume.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        for(int i=0;i<dd.length;i++) dd[i]=0;
        return view;
    }
    public void StartProgressBar()
    {
        label:
        myHandler = new Handler();
        accum=0;
        myProgressBar.setMax(1000);
        myProgressBar.setProgress(1);

        myHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run()
            {
                if(accum<=myProgressBar.getMax())
                {
                    if(run) {
                        myProgressBar.incrementProgressBy(progressStep);
                        accum++;
                    }
                    myHandler.postDelayed(runnable, 10);
                }
                else
                    _container.Action("REFRESH");
            }
        };
        myHandler.post(runnable);
    }
    private  void updateContent()
    {
        DatabaseReference myref=FirebaseDatabase.getInstance().getReference();
        myref.child("DB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for(DataSnapshot dts:dataSnapshot.getChildren())
                {
                    DataMode1234 data=dts.getValue(DataMode1234.class);
                    mData.add(data);
                }
                Random rd=new Random();
                int index=rd.nextInt(mData.size());
                while(true)
                {
                    if(dd[index]==1)
                    {
                        index=rd.nextInt(mData.size());
                    }
                    else
                    {
                        dd[index]=1;
                        break;
                    }
                }
                mQuestion = mData.get(index).getSound();
                mAnswer = mData.get(index).getWordE();
                realAnswer=mAnswer;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void InfoToHandle(String mess, String roundOfMode,String answer, String question,String Trans,String deFine,String[]answerBtn) {
        if(mess.equals("NEW"))
        {
            editTextAnswer.setHint("Your Answer");
            editTextAnswer.setEnabled(true);
            editTextAnswer.setHintTextColor(getResources().getColor(R.color.Black));
            btnHint.setEnabled(true);
            btnDone.setEnabled(true);
            textViewRound.setText(roundOfMode+ "/20");
            editTextAnswer.setText("");
            i=0;
            mQuestion = question;
            realAnswer=answer;

            //updateContent();
            StartProgressBar();
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == btnVolume.getId())
        {
            MediaPlayer mediaPlayer=new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(mQuestion);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
                   }
                });
        }

        if(v.getId() == btnDone.getId())
        {
            //Lấy ra câu trả lời
            String result = editTextAnswer.getText().toString();
            //Kiểm tra nếu khác rỗng
            if(!result.equals(""))
            {

                if(result.equalsIgnoreCase(realAnswer))
                {
                    point+=20;
                    //Toast.makeText(context, "Right...", Toast.LENGTH_SHORT).show();
                    mainRoundMode4.Action("RIGHT");
                    textViewPoint.setText(point+"");
                    resID = R.raw.sound_right;
                   // soundManager.loadSound(context,resID);
                   // soundManager.playClickSound();
                }
                else{
                    //Toast.makeText(context, "Wrong...", Toast.LENGTH_SHORT).show();
                    mainRoundMode4.Action("WRONG");
                    resID = R.raw.sound_wrong;
                   // soundManager.loadSound(context,resID);
                   // soundManager.playClickSound();
                }
                btnHint.setEnabled(false);
                btnDone.setEnabled(false);
                //Yêu cầu Activity RoundMode4 gửi round về để chuyển vòng và cập nhập dữ liệu.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    //Xử lý bằng thời gian chuyển màn
                    @Override
                    public void run() {
                        // Hide your View after 3 seconds
                        mainRoundMode4.Action("REFRESH");
                        //Trong khi thời gian chuyển màn hình(Round), cũng chính là thời gian show GifImage => không cho nhập
                        //editTextAnswer.setEnabled(false);
                    }
                }, 1000);

                myHandler.removeCallbacks(runnable);
            }
            //Nếu rỗng, thì gửi 1 thông báo rỗng lên Round để hiện Toast Custom nên ko hiện ở Fragment đươc
           else
            {
                mainRoundMode4.Action("NOTIFICATION");
            }

        }
        //Trường hợp user dùng hint, xong dùng text nhập đáp án, xong lại dùng hint ???
        //Press Button Hint
        if(v.getId()==btnHint.getId())
        {
            res =  splitrealAnswer(realAnswer);
            editTextAnswer.setText("");
            String notification="";


            //Handler Split String: Hint: 3/5;
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
                textViewNumberHint.setText("Hint: "+strHint+"/5");
                //Show your view
                editTextAnswer.setVisibility(View.VISIBLE);
                String text="";
                for(int j=0; j<i; j++)
                {
                    text = text + "_ ";
                }
                text = text + res[i].toString();
                editTextAnswer.setHint(text);
                int color=getResources().getColor(R.color.MidnightBlue);
                editTextAnswer.setHintTextColor(color);
                i++;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // Hide your View after 3 seconds
                        editTextAnswer.setHint("");
                    }
                }, 1500);

                if(i==realAnswer.length()){
                    i=0;
                }

            }
            else
                {
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
                            if(point>=7)
                            {
                                point -=7;
                                hint++;;
                                String text = "Hint: " +  hint + "/5";
                                String txtpoint = point + "";
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
           // Toast.makeText(context, notification, Toast.LENGTH_SHORT).show();
        }
    }
    //Cắt chuỗi realAnswer, lưu từng ký tự và mảng res[];
    public Character[] splitrealAnswer(String _realAnswer){
        String str = _realAnswer;
        //Cắt String: answer thành các ký tự lưu vào mảng các ký tự res
        res = new Character[str.length()];
        for (int i = 0; i < str.length(); i++) {
            res[i] = str.charAt(i);
        }
        return res;
    }
    @Override
    public void onPause()
    {
        super.onPause();
        run = false;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        run = true;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        myHandler.removeCallbacks(runnable);
    }
}
