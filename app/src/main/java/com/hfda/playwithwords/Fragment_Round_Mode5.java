package com.hfda.playwithwords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Fragment_Round_Mode5 extends Fragment implements fromContainerToFrag, View.OnClickListener, AdapterView.OnItemClickListener {

    ProgressBar myProgressBar;
    Round _container; //Activity chứa Fragment

    GridView initGridView;
    GridView addGridView;
    TextView textViewQuestion;
    TextView textViewRound;
    Button bntCheck;
    TextView textViewPoint;
    ArrayAdapter<String> addarrayAdapter;
    ArrayAdapter<String> initarrayAdapter;
    final List<Mode5> mangData=new ArrayList<>();
    List<String> listinit;
    List<String> listadd;
    ImageButton imgBntHint;
    TextView tvHint;
    int countQuestion=0;
    public int index=1;
    int randomIndex[] = new int[31];
    int point=0;
    int hint=5;
    private int number_rows;
    private String myEnglish="";
    private String myVietnam="";
    private String[] listEnglish;
    Handler myHandler;
    Runnable runnable;
    boolean run;
    int accum;
    int progressStep=1;
    int[] dd1=new int[30];
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
    private void updateContent()
    {

            DatabaseReference myref=FirebaseDatabase.getInstance().getReference();
            myref.child("DBmode5").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mangData.clear();
                    for(DataSnapshot dts:dataSnapshot.getChildren())
                    {
                        Mode5 mode5=dts.getValue(Mode5.class);
                        mangData.add(mode5);
                    }
                    Random rd=new Random();
                    int index=rd.nextInt(mangData.size());
                    while(true)
                    {
                        if(dd1[index]==1)
                        {
                            index=rd.nextInt(mangData.size());
                        }
                        else
                        {
                            dd1[index]=1;
                            break;
                        }
                    }
                    myVietnam=mangData.get(index).getVN();
                    myEnglish=mangData.get(index).getTA();
                    textViewQuestion.setText(myVietnam);
                    listEnglish = myEnglish.split(" ");
                    String[] randlistEnglish = myEnglish.split(" ");
                    Collections.shuffle(Arrays.asList(randlistEnglish));
                    String[] demo = new String[]{};
                    listinit = new ArrayList<String>(Arrays.asList(randlistEnglish));
                    listadd = new ArrayList<String>(Arrays.asList(demo));
                    initarrayAdapter
                            = new ArrayAdapter<String>(_container, R.layout.mode5_custom_listitem, R.id.tvText, listinit);
                    addarrayAdapter
                            = new ArrayAdapter<String>(_container, R.layout.mode5_custom_listitem, R.id.tvText, listadd);
                    initGridView.setAdapter(initarrayAdapter);
                    addGridView.setAdapter(addarrayAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


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

    @Override
    public void onClick(View v) {


       if(v.getId()==bntCheck.getId()){
            bntCheck.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String answerss=new String();
                    for(int i=0;i<listadd.size();i++){
                        answerss+=listadd.get(i);
                        if(i!=listadd.size()-1){
                            answerss+=" ";
                        }
                    }
                    if(answerss.compareTo(myEnglish)==0){
                       /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_container);
                        alertDialogBuilder.setMessage("Correct");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();*/
                     //  bntCheck.setBackground(getResources().getDrawable(R.drawable.btn_mode5_press));

                        point+=20;
                        textViewPoint.setText(String.valueOf(point));

                        _container.Action("RIGHT");
                       // myProgressBar.setProgress(countQuestion*100/20);
                    }
                    else{

                      //  bntCheck.setBackground(getResources().getDrawable(R.drawable.btn_wrong));
                        _container.Action("WRONG");
                        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_container);
                        alertDialogBuilder.setMessage("Error");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();*/
                    }
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
            });
        }
        else if(v.getId()==imgBntHint.getId())
        {
           imgBntHint.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(hint==1)
                   {
                       if(Round.flag==0)
                       {
                           Round.HienThongBaoMuaHint(getContext());
                       }
                   }
                   if(hint>0)
                   {
                       boolean ch=false;
                       int posCurrent=0;
                       String[] demo = new String[]{};
                       listinit = new ArrayList<String>(Arrays.asList(demo));

                       int i;
                       for(i=0;i<listadd.size();i++){
                           if(listadd.get(i).compareTo(listEnglish[i])==0){
                               listadd.add(listEnglish[i]);
                           }
                           else{
                               break;
                           }
                       }

                       listadd = new ArrayList<String>(Arrays.asList(demo));
                       for(int j=0;j<=i;j++) {
                           listadd.add(listEnglish[j]);
                       }
                       i++;

                       for(;i<listEnglish.length;i++){
                           listinit.add(listEnglish[i]);

                       }
                       initarrayAdapter
                               = new ArrayAdapter<String>(_container, R.layout.mode5_custom_listitem, R.id.tvText, listinit);
                       addarrayAdapter
                               = new ArrayAdapter<String>(_container, R.layout.mode5_custom_listitem, R.id.tvText, listadd);
                       hint--;
                       tvHint.setText("Hint: "+String.valueOf(hint)+"/5");
                       initGridView.setAdapter(initarrayAdapter);
                       addGridView.setAdapter(addarrayAdapter);
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
                                   hint++;
                                   String text = "Hint: " + hint + "/5";
                                   String txtpoint = point + "";
                                   tvHint.setText(text);
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

               }
           });
       }
    }

    @Override
    public void InfoToHandle(String mess, String roundOfMode,String answer, String question,String Trans,String deFine,String[]answerBtn) {
        if(mess.equals("NEW")) //Activity gửi thông diệp xuống kêu set lại dữ liệu trên màn hình cho vòng chơi mới
        {
          //  bntCheck.setBackground(getResources().getDrawable(R.drawable.my_button_mode5));
            index++;
            countQuestion++;
            textViewRound.setText(countQuestion+"/20");
            updateContent();
            StartProgressBar();
            Log.d("AA",mangData.size()+"");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_round_mode5, container, false);
        _container = (Round)getActivity();
        textViewQuestion = (layout).findViewById(R.id.textViewQuestion);
        initGridView = (layout).findViewById(R.id.initGridView);
        addGridView = (layout).findViewById(R.id.addGridView);
        bntCheck = (layout) .findViewById(R.id.btnCheck);
        textViewRound = (layout).findViewById(R.id.textViewRound);
        myProgressBar = (layout).findViewById(R.id.myProgressBar);
        textViewPoint=(layout).findViewById(R.id.textViewPoint);
        tvHint = (layout).findViewById(R.id.textViewNumberHint);
        imgBntHint = (layout).findViewById(R.id.btnHint);
        for(int i=1;i<=30;i++){
            randomIndex[i]=1;
        }
        for(int i=0;i<dd1.length;i++) dd1[i]=0;
        Collections.shuffle(Arrays.asList(randomIndex));
       updateContent();

        initGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = initGridView.getItemAtPosition(position);

                String getStr = (String) o;
                listadd.add(listadd.size(), getStr);
                addarrayAdapter.notifyDataSetChanged();
                listinit.remove(getStr);
                initarrayAdapter.notifyDataSetChanged();
            }
        });

        addGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = addGridView.getItemAtPosition(position);

                String getStr = (String) o;
                listinit.add(listinit.size(), getStr);
                initarrayAdapter.notifyDataSetChanged();
                listadd.remove(getStr);
                addarrayAdapter.notifyDataSetChanged();
            }
        });


//        initGridView.setOnItemClickListener(this);
//        addGridView.setOnItemClickListener(this);
        bntCheck.setOnClickListener(this);
        imgBntHint.setOnClickListener(this);
        _container.Action("REFRESH");
        return layout;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        if(v.getId()==initGridView.getId()) //người dùng bấm chọn hint
        {
            initGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object o = initGridView.getItemAtPosition(position);

                    String getStr = (String) o;
                    listadd.add(listadd.size(), getStr);
                    addarrayAdapter.notifyDataSetChanged();
                    listinit.remove(getStr);
                    initarrayAdapter.notifyDataSetChanged();
                }
            });
        }
        else if(v.getId()==addGridView.getId()){
            addGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object o = addGridView.getItemAtPosition(position);

                    String getStr = (String) o;
                    listinit.add(listinit.size(), getStr);
                    initarrayAdapter.notifyDataSetChanged();
                    listadd.remove(getStr);
                    addarrayAdapter.notifyDataSetChanged();
                }
            });
        }
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

