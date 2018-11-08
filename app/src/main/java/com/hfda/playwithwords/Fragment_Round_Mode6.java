package com.hfda.playwithwords;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

public class Fragment_Round_Mode6 extends Fragment implements fromContainerToFrag, View.OnClickListener {
    Round _container;
    ProgressBar myProgressBar;
    static int countQuestion=0;
    private int count=0;
    TextView textViewRound;
    @Override
    public void onClick(View v) {

    }
    @Override
    public void InfoToHandle(String mess, String roundOfMode, Object question, String answer, String transcription, String[] answerInBtn) {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_round_mode6, container, false);
        myProgressBar = (layout).findViewById(R.id.myProgressBar);
        textViewRound = (layout).findViewById(R.id.textViewRound);
        final RippleBackground rippleBackground=(layout).findViewById(R.id.content);
        final ImageView button=layout.findViewById(R.id.centerImage);
        _container = (Round)getActivity();
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN) {
                    button.setSelected(true);
                    rippleBackground.startRippleAnimation();
                    return true;
                } else if(event.getAction()==MotionEvent.ACTION_UP){
                    button.setSelected(false);
                    rippleBackground.stopRippleAnimation();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_container);

                    if(count%3==0 || count%3==1){
                        alertDialogBuilder.setMessage("Correct");
                        countQuestion++;
                        if(countQuestion>20){
                            countQuestion--;
                        }
                        textViewRound.setText(countQuestion+"/20");
                        myProgressBar.setProgress(countQuestion*100/20);
                    }
                    else{
                        alertDialogBuilder.setMessage("Failure");
                    }
                    count++;
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;
                }

                return false;
            }
        });
        return layout;
    }

}
