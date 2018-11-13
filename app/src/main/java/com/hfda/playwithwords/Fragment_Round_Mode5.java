package com.hfda.playwithwords;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

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
    Data_Mode5 dataMode5;
    ArrayAdapter<String> addarrayAdapter;
    ArrayAdapter<String> initarrayAdapter;
    List<String> listinit;
    List<String> listadd;
    ImageButton imgBntHint;
    TextView tvHint;
    AlertDialog.Builder alertDialogCorrect;
    AlertDialog.Builder alertDialogError;
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
    int accum;
    int progressStep=1;

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
    public void updateContent(){
        try {
            SQLiteOpenHelper myDatabase = new MyDatabase(_container.getApplicationContext());
            SQLiteDatabase db = myDatabase.getReadableDatabase();

//            Cursor cursor = db.query("DATA",
//                    new String[]{"COUNT(ID) AS NUM_ROW"},
//                    null, null, null, null, null); //truy vấn số lượng dòng
//
//            if(cursor.moveToFirst())//di chuyển con trỏ lên dòng đầu tiên của bảng kết quả
//            {
//                number_rows = cursor.getInt(0); //lấy dữ liệu ở cột thứ 0, kiểu int
//            }

            Cursor cursor = db.query("MODE5",
                    null,
                    "ID = ?", new String[]{String.valueOf(index)}, null, null, null); //truy vấn số lượng dòng

            if (cursor.moveToFirst())//di chuyển con trỏ lên dòng đầu tiên của bảng kết quả
            {
                myEnglish = cursor.getString(2);
                myVietnam = cursor.getString(1);
            }
            cursor.close();
            db.close();
        }catch(SQLException ex) {
            Log.e("Error", "An error occured when trying to access database!");
        }
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
                       bntCheck.setBackground(getResources().getDrawable(R.drawable.btn_mode5_press));

                        point+=20;
                        textViewPoint.setText(String.valueOf(point));

                        _container.Action("RIGHT");
                       // myProgressBar.setProgress(countQuestion*100/20);
                    }
                    else{

                        bntCheck.setBackground(getResources().getDrawable(R.drawable.btn_wrong));
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
        }else if(v.getId()==imgBntHint.getId()){
           imgBntHint.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(hint<=0){
                       return;
                   }
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
           });
       }
    }

    @Override
    public void InfoToHandle(String mess, String roundOfMode, Object question, Object answer, String transcription, String[] answerInBtn) {
        if(mess.equals("NEW")) //Activity gửi thông diệp xuống kêu set lại dữ liệu trên màn hình cho vòng chơi mới
        {
            bntCheck.setBackground(getResources().getDrawable(R.drawable.my_button_mode5));
            index++;
            countQuestion++;
            textViewRound.setText(countQuestion+"/20");
            updateContent();
            StartProgressBar();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_round_mode5, container, false);
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
}

