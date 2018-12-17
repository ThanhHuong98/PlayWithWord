package com.hfda.playwithwords;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Setting extends Fragment implements View.OnClickListener
{
   ImageButton btnSpeaker;
   ImageButton btnUpdateProfile;
    boolean isPlay=true;
    Menu _container;
    public Fragment_Setting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment__setting, container, false);
        _container=(Menu) getActivity();
        btnSpeaker=v.findViewById(R.id.ic_sound);
        btnUpdateProfile=v.findViewById(R.id.ic_profile);
        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==btnSpeaker.getId()){
                    isPlay=!isPlay;

                    if(isPlay==true){

                       _container.Action("SOUND");
                        btnSpeaker.setBackgroundResource(R.drawable.ic_speaker);
                    }
                    else{
                        _container.Action("MUTE_SOUND");
                        btnSpeaker.setBackgroundResource(R.drawable.ic_mute_audio);

                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onClick(View v)
    {
//        if(v.getId()==btnSpeaker.getId()){
//            isPlay=!isPlay;
//
//            if(isPlay==true){
//
//                mediaPlayer.start();
//                mediaPlayer.setLooping(true);
//               btnSpeaker.setBackgroundResource(R.drawable.ic_speaker);
//            }
//            else{
//                btnSpeaker.setBackgroundResource(R.drawable.ic_mute_audio);
//                mediaPlayer.stop();
//            }
//        }
//        if(v.getId()==btnUpdateProfile.getId()){
//            //Fragment fragment_profile=new Fr
//        }
    }
}
