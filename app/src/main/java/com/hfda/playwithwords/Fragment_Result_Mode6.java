package com.hfda.playwithwords;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Result_Mode6 extends Fragment implements fromContainerToFrag,View.OnClickListener {

    Button btn_share;
    ImageButton btn_finish;

    TextView textViewPoint;
    TextView textViewRound;

    Result _container;
    public Fragment_Result_Mode6() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _container = (Result)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment__result__mode6, container, false);



        btn_share = (layout).findViewById(R.id.btn_share);
        btn_finish = (layout).findViewById(R.id.btn_finish);
        textViewPoint = (layout).findViewById(R.id.score);
        textViewRound = (layout).findViewById(R.id.result);

        btn_finish.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        _container.Action("REFRESH");
        return layout;    }

    @Override
    public void onClick(View v) {
        if(v.getId()==btn_share.getId())
        {
            _container.Action("SHARE");
        }
        if(v.getId()==btn_finish.getId())
        {
            _container.Action("FINISH");
        }
    }

    @Override
    public void InfoToHandle(String mess, String round, Object question, Object answer, String transcription, String[] answerInBtn) {
        textViewPoint.setText(mess);
        textViewRound.setText(round + "/20");
    }
}
