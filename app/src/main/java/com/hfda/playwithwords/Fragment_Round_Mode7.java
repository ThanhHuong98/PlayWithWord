package com.hfda.playwithwords;

import android.app.Dialog;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class Fragment_Round_Mode7 extends Fragment implements fromContainerToFrag, View.OnClickListener, AdapterView.OnItemClickListener {

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
    int[] dd1=new int[50];
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

    @Override
    public void onClick(View v) {



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

        _container.Action("REFRESH");
        return layout;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

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

