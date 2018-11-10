package com.hfda.playwithwords;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_Round_Mode4 extends Fragment implements fromContainerToFrag, View.OnClickListener{
    ImageButton btnVolume;
    EditText editTextAnswer;
    Button btnDone;
    ImageButton btnHint;
    TextView textViewNumberHint;
    TextView textViewPoint;
    TextView textViewRound;
    ProgressBar myProgressBar;

    int question;
    int resID;
    Context context;
    Round _container;

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

        return view;
    }
    public void StartProgressBar()
    {
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
                    myProgressBar.incrementProgressBy(progressStep);
                    accum++;
                    myHandler.postDelayed(runnable, 10);
                }
                else
                    _container.Action("REFRESH");
            }
        };
        myHandler.post(runnable);
    }
    @Override
    public void InfoToHandle(String mess, String roundOfMode, Object newQuestion, Object answer, String transcription, String[] answerInBtn) {
        if(mess.equals("NEW"))
        {
            editTextAnswer.setHint("Your Answer");
            editTextAnswer.setEnabled(true);
            editTextAnswer.setTextColor(getResources().getColor(R.color.Black));
            btnHint.setEnabled(true);
            btnDone.setEnabled(true);
            textViewRound.setText(roundOfMode+ "/20");
            textViewPoint.setText(point+"");
            editTextAnswer.setText("");
            question = (int)newQuestion;
            soundManager.loadSound(context,question);
            realAnswer = (String)answer;
            StartProgressBar();
        }
        /*if(mess.equals("RIGHTSOUND"))
        {
            resID = R.raw.sound_right;
            soundManager.loadSound(context,resID);
        }*/
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == btnVolume.getId())
        {
            soundManager.playClickSound();
        }
        if(v.getId() == btnDone.getId())
        {

            String result = editTextAnswer.getText().toString();
            if(!result.equals(""))
            {

                if(result.equalsIgnoreCase(realAnswer))
                {
                    point+=20;
                    //Toast.makeText(context, "Right...", Toast.LENGTH_SHORT).show();
                    mainRoundMode4.Action("RIGHT");
                    resID = R.raw.sound_right;
                    soundManager.loadSound(context,resID);
                    soundManager.playClickSound();
                }
                else{
                    //Toast.makeText(context, "Wrong...", Toast.LENGTH_SHORT).show();
                    mainRoundMode4.Action("WRONG");
                    resID = R.raw.sound_wrong;
                    soundManager.loadSound(context,resID);
                    soundManager.playClickSound();
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
            else
            {
                Toast.makeText(context, "You must enter at least one character", Toast.LENGTH_SHORT).show();
            }
        }
        //Trường hợp user dùng hint, xong dùng text nhập đáp án, xong lại dùng hint ???
        //Press Button Hint
        if(v.getId()==btnHint.getId())
        {
            res =  splitrealAnswer(realAnswer);
            editTextAnswer.setText("");
            String notification="";
            hint--;
            String strHint=hint+"";
            //Handler Split String: Hint: 3/5;
            if(hint>=0)
            {
                textViewNumberHint.setText("Hint: "+strHint+""+"/5");
                notification="Decrease the number of Hint...";
                //Show your view
                editTextAnswer.setVisibility(View.VISIBLE);
                String text="";
                for(int j=0; j<i; j++)
                {
                    text = text + "_ ";
                }
                text = text + res[i].toString();
                editTextAnswer.setHint(text);
                int color=getResources().getColor(R.color.BlueViolet);
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
            else{
                notification="You must use Pont to buy Hint..... ";
            }
            Toast.makeText(context, notification, Toast.LENGTH_SHORT).show();
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
}
