package com.hfda.playwithwords;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Setting extends Fragment implements View.OnClickListener
{
    public static final String CHECKMUS = "music";
    private static final int RESULT_LOAD_IMG = 1;
    private static final int RESULT_OK = 2;
    private static final int GALLERY_REQUEST = 3;
    ImageButton btnSpeaker;
    Context context;
    ImageButton btnUpdateProfile;
    ImageButton btnUpVolume;
    ImageButton btnDownVolume;
    boolean isPlay=true;
    Menu _container;


    public Fragment_Setting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment__setting, container, false);
        _container=(Menu) getActivity();
        context=getActivity();
        btnUpVolume=v.findViewById(R.id.ic_volume_up);
        btnDownVolume=v.findViewById(R.id.ic_volume_down);
        btnSpeaker=v.findViewById(R.id.ic_sound);

        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==btnSpeaker.getId()){
                    isPlay=!isPlay;

                    if(isPlay==true){

                        _container.Action("SOUND");
                        btnSpeaker.setBackgroundResource(R.drawable.ic_sound);
                    }
                    else{
                        _container.Action("MUTE_SOUND");
                        btnSpeaker.setBackgroundResource(R.drawable.ic_mute_sound);
                    }
                }
            }
        });
        btnUpVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _container.Action("UP");
            }
        });
        btnDownVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _container.Action("DOWN");
            }
        });

<<<<<<< HEAD

=======
>>>>>>> 274dbe1174e3bf2ecdfd82050bea1f9e5e569bc3
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
