package com.hfda.playwithwords;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_Round_Mode5 extends Fragment implements fromContainerToFrag, View.OnClickListener {

    ProgressBar myProgressBar;
    Round _container; //Activity chứa Fragment

    GridView initGridView;
    GridView addGridView;
    TextView textViewQuestion;
    TextView textViewRound;
    Button bntCheck;
    Data_Mode5 dataMode5;
    ArrayAdapter<String> addarrayAdapter;
    ArrayAdapter<String> initarrayAdapter;
    List<String> listinit;
    List<String> listadd;
    AlertDialog.Builder alertDialogCorrect;
    AlertDialog.Builder alertDialogError;
    static int countQuestion=0;
    @Override
    public void onClick(View v) {



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
    }

    @Override
    public void InfoToHandle(String mess, String roundOfMode, Object question, String answer, String transcription, String[] answerInBtn) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_round_mode5, container, false);

        textViewQuestion = (layout).findViewById(R.id.textViewQuestion);
        initGridView = (layout).findViewById(R.id.initGridView);
        addGridView = (layout).findViewById(R.id.addGridView);
        bntCheck = (layout) .findViewById(R.id.btnCheck);
        textViewRound = (layout).findViewById(R.id.textViewRound);
        myProgressBar = (layout).findViewById(R.id.myProgressBar);

        String a = new String("Toi");
        String b = new String("muon");
        String c = new String("doc");
        String d = new String("bao");
        String[] demo = new String[]{a, b, c, d};
        String[] demo1 = new String[]{};
        listinit = new ArrayList<String>(Arrays.asList(demo));
        listadd = new ArrayList<String>(Arrays.asList(demo1));

        _container = (Round)getActivity();
        initarrayAdapter
                = new ArrayAdapter<String>(_container, R.layout.mode5_custom_listitem, R.id.tvText, listinit);
        addarrayAdapter
                = new ArrayAdapter<String>(_container, R.layout.mode5_custom_listitem, R.id.tvText, listadd);
        initGridView.setAdapter(initarrayAdapter);
        addGridView.setAdapter(addarrayAdapter);

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
                if(answerss.compareTo("Toi muon doc bao")==0){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_container);
                    alertDialogBuilder.setMessage("Correct");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    countQuestion++;
                    if(countQuestion>20){
                        countQuestion--;
                    }
                    textViewRound.setText(countQuestion+"/20");
                    myProgressBar.setProgress(countQuestion*100/20);
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_container);
                    alertDialogBuilder.setMessage("Error");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });
        return layout;
    }



}

