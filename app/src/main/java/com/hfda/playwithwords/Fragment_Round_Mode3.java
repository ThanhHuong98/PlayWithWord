package com.hfda.playwithwords;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Round_Mode3 extends Fragment implements fromContainerToFrag, View.OnClickListener
{
    Button btnCheck;
    ImageButton btnHint;
    TextView textPoint;
    TextView numberRound;
    TextView textViewQuestion;
    TextView hint;
    TextView textHint;
    EditText editText_Answer3;
    ProgressBar myProgressBar;
    LinearLayout layout;
    Round _container; //Activity chứa Fragment
    int []dd=new int[50];
    Context context;
    int numberHint=5; //số hint tối đa cho người dùng
    int point=0; //điểm người dùng có được
    int indexOfHint=0;
    int accum;
    int question;
    int progressStep=1;
    Handler myHandler;
    Runnable runnable;
    boolean run = true;
    //String resultToHint=new String();
    String realAnswer;
    Character[] res;
    List<DataMode1234> mData=new ArrayList<>();
    String mQuestion;
    String mAnswer;

    public Fragment_Round_Mode3()
    {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
         layout = (LinearLayout)inflater.inflate(R.layout.fragment_round_mode3, container, false);
        //LinearLayout setBackg=(layout).findViewById(R.id.background_3);
        super.onCreate(savedInstanceState);
        _container = (Round)getActivity();
        btnHint=(layout).findViewById(R.id.btnHint);
        textPoint= (layout).findViewById(R.id.textViewPoint);
        numberRound = (layout).findViewById(R.id.textViewRound);
        textViewQuestion = layout.findViewById(R.id.textViewQuestion);
        textHint=layout.findViewById(R.id.textHint);
        hint = layout.findViewById(R.id.textViewNumberHint);
        myProgressBar = layout.findViewById(R.id.progressbar);
        btnCheck=(layout).findViewById(R.id.btnCheck);
        editText_Answer3=(layout).findViewById(R.id.editText_Answer3);
        btnHint.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        context=getActivity().getApplicationContext();
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
                    if(run)
                    {
                        if(Round.isStart==true) {
                            myProgressBar.incrementProgressBy(progressStep);
                            accum++;
                        }
                        else
                        {
                            myProgressBar.incrementProgressBy(0);
                        }
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
            layout.setBackground(ContextCompat.getDrawable(_container,R.drawable.mode3_hint));

            res =  splitrealAnswer(realAnswer);
            //editText_Answer3.setText("");
            String notification="";

            String strHint=numberHint+"";

            if(numberHint==1)
            {
                if(Round.flag==0)
                {
                    Round.HienThongBaoMuaHint(getContext());
                }
            }
            if(numberHint>0) //nếu còn trợ giúp
            {
                hint.setText("Hint: " + strHint +"/5");
                notification="Decrease the number of hints...";
                editText_Answer3.setVisibility(v.VISIBLE);
                String resultToHint = "";

                   for (int i = 0; i < indexOfHint; i++) {
                        resultToHint = resultToHint + "_ ";
                   }

                resultToHint=resultToHint+res[indexOfHint].toString();
                editText_Answer3.setHint(resultToHint);
                int color=getResources().getColor(R.color.BlueViolet);
                editText_Answer3.setHintTextColor(color);
                indexOfHint++;

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // Hide your View after 3 seconds
                        editText_Answer3.setHint("");
                        layout.setBackground(ContextCompat.getDrawable(_container,R.drawable.background_mode3));

                    }
                }, 1000);

                if(indexOfHint == realAnswer.length()){

                    indexOfHint = 0;
                }
                numberHint--;
                String text = "Hint: " + numberHint + "/5";
                hint.setText(text);
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
                Round.isStart=false;
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
                            Round.isStart=true;
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
                                    Round.isStart=true;
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
                        Round.isStart=true;
                        dialog.dismiss();
                    }
                });
                tvHey.setText("Hey,");
                tvNofitication.setText("Just 7 points for 1 hint.\nDo you really want to buy more hint?");
                dialog.show();
            }

        }
       // String result=editText_Answer3.getText().toString();

        if(v.getId()==btnCheck.getId()) {
            String result = editText_Answer3.getText().toString();
            if (!result.equals("")) {
                if (result.equalsIgnoreCase(realAnswer)) //đáp án người dùng chọn trùng với đáp án chính
                {
                    //set background cho cái nút mà người dùng chọn thành màu xanh
                    editText_Answer3.setTextColor(getResources().getColor(R.color.SpringGreen));
                    editText_Answer3.setText(result);
                    btnCheck.setBackground(this.getResources().getDrawable(R.drawable.btn_right));
                    //tăng điểm lên
                    point += 20;

                    String text = point + "";
                    //setText cho cái TextView Point(s)
                    textPoint.setText(text);
                    _container.Action("RIGHT");
                } else //đáp án người dùng chọn khác với đáp án chính
                {
                    btnCheck.setBackgroundResource(R.drawable.btn_wrong);
                    editText_Answer3.setTextColor(getResources().getColor(R.color.Red));
                    editText_Answer3.setText(result);
                    _container.Action("WRONG");
                }

                btnCheck.setEnabled(false);

                btnHint.setEnabled(false);

                //cho progressbar dừng lại không chạy nữa
                myHandler.removeCallbacks(runnable);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //sau 1s nó sẽ gửi thông điệp REFRESH lên trên Activity để lấy dữ liệu câu hỏi + đáp án mới
                        //về set lại trên màn hình
                        _container.Action("REFRESH");
                    }
                }, 1000);

            } else {
                Toast.makeText(context, "You must enter at least one character!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*
    phương thức message trong Interface fromContainerToFrag sẽ được kích hoạt khi mà Activity gửi dữ liệu xuống Fragment*/
    private void updateContent()
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
                mAnswer=mData.get(index).getWordE();
                mQuestion=mData.get(index).getDefinition();
                textViewQuestion.setText((String)mQuestion);
                realAnswer=mAnswer;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void InfoToHandle(String mess, String round,String answer,String question,String c,String d,String[] e)
    {

        if(mess.equals("NEW")) //Activity gửi thông diệp xuống kêu set lại dữ liệu trên màn hình cho vòng chơi mới
        {
            int color=getResources().getColor(R.color.Black);
            editText_Answer3.setTextColor(color);
            editText_Answer3.setText("");
            editText_Answer3.setEnabled(true);
            btnCheck.setText("Check");
            indexOfHint = 0;
            int colorHint=getResources().getColor(R.color.Gray);
            editText_Answer3.setHintTextColor(colorHint);
            editText_Answer3.setHint("__ __ __ __");
            String text= round + "/20";
            if(numberRound!=null)
                numberRound.setText(text);

            textViewQuestion.setText((String)question);
            realAnswer=answer;

            //updateContent();
            if(btnCheck!=null)
            {
                btnCheck.setBackground(this.getResources().getDrawable(R.drawable.button_check3));
            }

            if(! btnCheck.isEnabled()){
                btnCheck.setEnabled(true);
            }
            if(!btnHint.isEnabled())
            {
                btnHint.setEnabled(true);
            }
            StartProgressBar();
        }
    }
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
