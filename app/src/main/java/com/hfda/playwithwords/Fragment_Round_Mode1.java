package com.hfda.playwithwords;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Round_Mode1 extends Fragment implements fromContainerToFrag, View.OnClickListener
{
    Button[] btnAnswer = new Button[4]; //mảng chứa 4 button đáp án
    Button userChosen;
    ImageButton btnHint;
    TextView textPoint;
    TextView numberRound;
    TextView textViewQuestion;
    TextView textViewTranscript;
    TextView hint;
    ProgressBar myProgressBar;
    boolean runable = true;
    List<DataMode1234> mData=new ArrayList<>();
    int[] dd=new int[30];
    String mQuestion;
    String mAnswer;
    String[] mAnswerButton=new String[4];
    String mTranscript;
    Round _container; //Activity chứa Fragment

    int numberHint=5; //số hint tối đa cho người dùng
    int point=0; //điểm người dùng có được

    int accum;
    int progressStep=1;
    Handler myHandler;
    Runnable runnable;
    Context context;
    String realAnswer;
    public Fragment_Round_Mode1()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_round_mode1, container, false);

        _container = (Round)getActivity();

        btnAnswer[0]=(layout).findViewById(R.id.btnAnswer1);
        btnAnswer[1]=(layout).findViewById(R.id.btnAnswer2);
        btnAnswer[2]=(layout).findViewById(R.id.btnAnswer3);
        btnAnswer[3]=(layout).findViewById(R.id.btnAnswer4);
        btnHint=(layout).findViewById(R.id.btnHint);
        textPoint= (layout).findViewById(R.id.textViewPoint);
        numberRound = (layout).findViewById(R.id.textViewRound);
        textViewQuestion = layout.findViewById(R.id.textViewQuestion);
        textViewTranscript = layout.findViewById(R.id.textViewTranscription);
        hint = layout.findViewById(R.id.textViewNumberHint);
        myProgressBar = layout.findViewById(R.id.myBarHor);
        context=getActivity().getApplicationContext();
        btnAnswer[0].setOnClickListener(this);
        btnAnswer[1].setOnClickListener(this);
        btnAnswer[2].setOnClickListener(this);
        btnAnswer[3].setOnClickListener(this);
        btnHint.setOnClickListener(this);
        for(int i=0;i<dd.length;i++) dd[i]=0;
        //Khi mới vào vòng đầu tiên thì phải gửi thông điệp lên Activity Round ở trên để nó gửi
        //thông điệp + database xuống cho mình set dữ liệu trên màn hình chơi
        _container.Action("REFRESH");

        return layout;
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
                    if(runable)
                    {
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


    @Override
    public void onClick(View v)
    {
        if(v.getId()==btnHint.getId()) //người dùng bấm chọn hint
        {
            if(numberHint==1)
            {
                if(Round.flag==0)
                {
                    Round.HienThongBaoMuaHint(getContext());
                }
            }
            if(numberHint>0) //nếu còn trợ giúp
            {
                //lam mo di 1 dap an sai
                for (Button e : btnAnswer) //coi trong 4 đáp án, cái nào sai thì ẩn nó đi
                {
                    //nếu đáp án trong button là sai và button đó chưa bị ẩn
                    // (cái này để cho trường hợp người dùng sử dụng nhiều hint trong 1 vòng)
                    if (!e.getText().toString().equalsIgnoreCase(realAnswer) && e.isEnabled()) {
                        numberHint--;
                        e.setEnabled(false);
                        e.setVisibility(View.INVISIBLE);
                        break;
                    }
                }

                String text = "Hint: " + numberHint + "/5";
                hint.setText(text);
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
                                numberHint++;
                                String text = "Hint: " + numberHint + "/5";
                                String txtpoint = point + "";
                                hint.setText(text);
                                textPoint.setText(txtpoint);

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
        }
        else //nguoi dung chon dap an
        {
            userChosen=(Button)v;

            if(userChosen.getText().equals(realAnswer)) //đáp án người dùng chọn trùng với đáp án chính
            {
                //set background cho cái nút mà người dùng chọn thành màu xanh
                userChosen.setBackground(this.getResources().getDrawable(R.drawable.btn_right));
                //tăng điểm lên
                point+=20;

                String text = point + "";
                //setText cho cái TextView Point(s)
                textPoint.setText(text);

                //phát nhạc "ting ting" báo hiệu người dùng đã chọn đúng
                    //từ từ xử lý sau =))
                _container.Action("RIGHT");
            }
            else //đáp án người dùng chọn khác với đáp án chính
            {
                userChosen.setBackground(this.getResources().getDrawable(R.drawable.btn_wrong));
                _container.Action("WRONG");
                //phát nhạc báo hiệu sai
                    //cái này cũng từ từ xử lý sau =)
            }
            //sau khi người dùng đã click chọn đáp án thì sẽ cho dừng khoảng 1s để hiện nút xanh xanh báo đúng này nọ
            //chứ không cho dừng thì nó nhảy qua màn khác ngay lập tức, người ta sẽ không thấy báo hiệu đúng/sai gì cả
            //trong lúc dừng 1s đó, có thể người dùng sẽ bấm vào đáp án khác nữa hoặc là bấm vào hint
            //do đó mình phải unenable mấy cái nút đó hết, khi nào chuyển qua câu hỏi mới thì set enable nó lại, để tránh
            //người dùng bấm lung tung
            for(Button e:btnAnswer)
            {
                e.setEnabled(false);
            }
            btnHint.setEnabled(false);

            //cho progressbar dừng lại không chạy nữa
            myHandler.removeCallbacks(runnable);

            //gửi cái đáp án người dùng chọn lên Activity để nó chỉnh sửa thông tin điểm ở trển để cuối cùng nó gửi
            //mấy cái thông tin điểm, số màn chơi người dùng làm đúng qua cho Activity Result thông qua intent

            //cho dừng khoảng 0.6s sau đó gửi thông điệp lên Activity ở trên để chuyển qua câu hỏi mới
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    //sau 1s nó sẽ gửi thông điệp REFRESH lên trên Activity để lấy dữ liệu câu hỏi + đáp án mới
                    //về set lại trên màn hình
                    _container.Action("REFRESH");
                }
            }, 1000);

        }
    }
    private void SufferStringArray(String[] arr)
    {
        for (int i = arr.length-1; i > 0; i--)
        {
            //tạo ra vị trí j ngẫu nhiên từ 0<=j<=i
            int j = (int)(Math.random() * (i + 1));
            //hoán vị arr[i] và arr[j]
            String temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    private  void updateContent()
    {
        DatabaseReference myref=FirebaseDatabase.getInstance().getReference();
        myref.child("DB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for(DataSnapshot dts: dataSnapshot.getChildren()) {
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
                mQuestion=mData.get(index).getWordE();
                mAnswer=mData.get(index).getWordV();
                mTranscript=mData.get(index).getPronunciation();
                ArrayList<Integer> rand = new ArrayList<>(); // chứa id của dòng chứa câu hỏi và 3 câu trả lời sai
                rand.add(index);
                mAnswerButton[0]=mAnswer;
                for(int i=1; i<=3; i++)
                {
                    int random = rd.nextInt(mData.size());
                    while(rand.indexOf(random)>=0)
                    {
                        random = rd.nextInt(mData.size());
                    }
                    rand.add(random);
                    mAnswerButton[i] = mData.get(random).getWordV();
                }
                //set lai cau hoi
                textViewQuestion.setText((String)mQuestion);
                //set lai phien am
                textViewTranscript.setText(mTranscript);
                SufferStringArray(mAnswerButton);

                //set lại dữ liệu cho 4 button đáp án, nếu nó có bị unenable hoặc bị ẩn thì cho nó bình thường trở lại
                for(int i=0; i<4; i++)
                {
                    if(!btnAnswer[i].isEnabled()) btnAnswer[i].setEnabled(true);
                    if(!btnAnswer[i].isShown()) btnAnswer[i].setVisibility(View.VISIBLE);
                    btnAnswer[i].setText(mAnswerButton[i]);
                }
                realAnswer = (String)mAnswer;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*
    phương thức message trong Interface fromContainerToFrag sẽ được kích hoạt khi mà Activity gửi dữ liệu xuống Fragment*/
    @Override
    public void InfoToHandle(String mess, String roundOfMode,String answer, String question,String Trans,String deFine,String[]answerBtn)
    {

        if(mess.equals("NEW")) //Activity gửi thông diệp xuống kêu set lại dữ liệu trên màn hình cho vòng chơi mới
        {

            String text= roundOfMode + "/20";
            numberRound.setText(text);
            textViewQuestion.setText((String)question);
            textViewTranscript.setText(Trans);
            SufferStringArray(answerBtn);
            for(int i=0; i<4; i++)
            {
                if(!btnAnswer[i].isEnabled()) btnAnswer[i].setEnabled(true);
                if(!btnAnswer[i].isShown()) btnAnswer[i].setVisibility(View.VISIBLE);
                btnAnswer[i].setText(answerBtn[i]);
            }

            realAnswer = (String)answer;
           // updateContent();
            //set cái background button đáp án người dùng chọ lúc nãy về bình thường, không còn xanh đỏ nữa
            if(userChosen!=null)
            {
                userChosen.setBackground(this.getResources().getDrawable(R.drawable.my_button));
            }

            if(!btnHint.isEnabled())
            {
                btnHint.setEnabled(true);
            }

            StartProgressBar();
        }
    }
    @Override
    public void onPause()
    {
        super.onPause();
        runable = false;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        runable = true;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        myHandler.removeCallbacks(runnable);
    }
}
