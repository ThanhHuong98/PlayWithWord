package com.hfda.playwithwords;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

    private SoundManager soundManager;
    SoundPool soundPool;

    int CLICK_SOUND;


    public SoundManager()
    {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            soundPool = (new SoundPool.Builder()).setMaxStreams(1).build();
        }else{
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        }
    }

    public void loadSound(Context context, int resID)
    {//R.raw.mp3
        CLICK_SOUND = soundPool.load(context, resID, 1);
        // load other sound if you like
    }

    public void playClickSound()
    {
        soundPool.play(CLICK_SOUND, 1.0F, 1.0F, 0, 0, 1.0F);
    }
}
