package com.hfda.playwithwords;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Round extends AppCompatActivity implements fromFragToContainer
{
    //set âm thanh nhạc nền trong 20 màn chơi
   private boolean mIsBound = false;
    private MusicService mServ;

    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder service) {
            // mServ = ((MusicService.ServiceBinder)binder).getService();
            MusicService.ServiceBinder binder = (MusicService.ServiceBinder)service;
            mServ =binder.getService();
            mIsBound=true;
        }

        public void onServiceDisconnected(ComponentName name) {
            // mServ = null;
            mIsBound=false;
        }
    };
    /*=============================================*/

    static final String MODE = "mode"; //Extra của intent, để nhận thông tin mode thứ mấy từ intent của Menu gửi qua

    Fragment_Round_Mode1 fragmentRound1;
    //Fragment_Round_Mode2 fragmentRound2;
    Fragment_Round_Mode3 fragmentRound3;
    Fragment_Round_Mode4 fragmentRound4;
    Fragment_Round_Mode5 fragmentRound5;
    Fragment_Round_Mode6 fragmentRound6;
    FragmentTransaction ft;
    int mode;
    int round = 0;
    int point = 0;
    int numberRightAnswer=0; //s&#x1ed1; c&acirc;u tr&#x1ea3; l&#x1edd;i &#x111;&uacute;ng
    GifImageView gifImageView;
    SoundManager notification = new SoundManager();

    //những thông tin cần set mới cho mỗi màn chơi trong 20 màn, tùy theo mỗi mode sẽ bỏ thêm
    //một vài đối tượng ở đây, vd như id hình ảnh, id âm thanh, chuỗi định nghĩa từ, ...
    //những đối tượng này sẽ được đọc ra từ database
    Object newQuestion; //
    String newAnswer;
    String newTransciption;
    String[] newAnswerInBtn = new String[4];

    //Ham de doc du lieu tu data base len
    private void readData()
    {
        /////
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        gifImageView=(GifImageView)findViewById(R.id.GifImageView);


        Intent intent = getIntent(); //nhận intent từ Menu gửi qua
        String m = intent.getStringExtra(MODE); //lấy Extra
        mode = Integer.parseInt(m); //chuyển Extra từ String sang int

        ft = getSupportFragmentManager().beginTransaction();

        //tùy theo thông tin mode mà Menu gửi qua, ta sẽ tạo ra 1 fragment theo mode đó để
        //thay vào FrameLayout
        switch(mode)
        {
            case 1:
                fragmentRound1 = new Fragment_Round_Mode1();
                ft.replace(R.id.fragment_round, fragmentRound1);
                break;
            case 2:
                //fragmentRound2 = new Fragment_Round_Mode2();
                //ft.replace(R.id.fragment_round, fragmentRound2);
                break;
            case 3:
                fragmentRound3 = new Fragment_Round_Mode3();
                ft.replace(R.id.fragment_round, fragmentRound3);
                break;
            case 4:
                fragmentRound4 = new Fragment_Round_Mode4();
                ft.replace(R.id.fragment_round, fragmentRound4);
                break;
            case 5:
                fragmentRound5 = new Fragment_Round_Mode5();
                ft.replace(R.id.fragment_round, fragmentRound5);
            case 6:
                fragmentRound6 = new Fragment_Round_Mode6();
                ft.replace(R.id.fragment_round, fragmentRound6);
        }
        ft.commit();
    }

    //Khi một item ở bên dưới Fragment_Round_ModeX được click thì interface này sẽ được kích hoạt
    //và nhận thông tin ở dưới Fragment gửi lên
    @Override
    //action là hành động mà Fragment gửi lên
    public void Action(String action)
    {
        if(action.equals("REFRESH")) //người dùng đã click chọn/điền đáp án, ta sẽ set lại toàn bộ dữ liệu mới cho màn hình
        {
            if (round < 20) //nếu còn trong 20 vòng thì mới set dữ liệu lại
            {
                round++;
                //thay đổi tất cả text trong button và TextView câu hỏi thành 1 text khác
                switch (mode)
                {
                    case 1:
                        //Activity Round sẽ gửi bộ dữ liệu gồm câu hỏi và 4 đáp án mới xuống cho Fragment set lại
                        //thông qua phương thức message của Interface fromContainerToFrag
                        newQuestion="Cat"; //
                        newAnswer="con mèo";
                        newTransciption ="/cæt/";
                        newAnswerInBtn[0]="con chó"; newAnswerInBtn[1]="con mèo"; newAnswerInBtn[2]="con gà"; newAnswerInBtn[3]="con heo";
                        fragmentRound1.InfoToHandle("NEW", round+"", newQuestion, newAnswer, newTransciption, newAnswerInBtn);
                        break;
                    case 2:
                        // fragmentRound2.InfoToHandle("NEW", ...);
                        break;
                    case 3:
                        newQuestion="This's name's bird about 6 characters";
                        newAnswer="chicken";
                        fragmentRound3.InfoToHandle("NEW", round+"", newQuestion, newAnswer, newTransciption, newAnswerInBtn);
                        break;
                    case 4:
                        newQuestion=R.raw.mushroom;
                        newAnswer="mushroom";
                        fragmentRound4.InfoToHandle("NEW" ,round+"", newQuestion,newAnswer, "",null);
                        break;
                }
            }
            else //nếu đã chơi đủ 20 màn thì tạo 1 intent để show kết quả
            {
                Intent intent = new Intent(this, Result.class);
                intent.putExtra(Result.FINAL_RESULT, numberRightAnswer + ""); //thông tin số vòng người chơi trả lời đúng
                intent.putExtra(Result.TOTAL_SCORE, point + ""); //thông tin số điểm
                intent.putExtra(Result.MODE, mode+"");
                startActivity(intent);
            }
        }
        if(action.equals("RIGHT"))
            //khi người dùng mới chọn đáp án, Fragment sẽ gửi thông tin đáp án mà người dùng chọn lên Activity  để
            //so sánh với đáp án của database ứng với câu hỏi
        {
            point +=20;
            numberRightAnswer++;

            notification.playClickSound();
            gifImageView.setVisibility(View.VISIBLE);
            gifImageView.setGifImageResource(R.drawable.gif_right);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                //Xử lý bằng thời gian chuyển màn
                @Override
                public void run()
                {
                    // Hide your View after 3 seconds
                    gifImageView.setVisibility(View.INVISIBLE);//VISIBLE, GONE
                }
            }, 2000);
        }
        if(action.equals("WRONG"))
        {

            notification.playClickSound();
            gifImageView.setVisibility(View.VISIBLE);
            gifImageView.setGifImageResource(R.drawable.gif_wrong);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                //Xử lý bằng thời gian chuyển màn
                @Override
                public void run() {
                    // Hide your View after 3 seconds
                    gifImageView.setVisibility(View.INVISIBLE);//VISIBLE, GONE
                }
            }, 3000);

        }
    }
   /*@Override
    protected void onStart()
   {
       super.onStart();
        // Tạo đối tượng Intent cho WeatherService.
      // Intent intent = new Intent(Round.this,MusicService.class);

        // Gọi method bindService(..) để giàng buộc dịch vụ với giao diện.
     //   this.bindService(intent, Scon, Context.BIND_AUTO_CREATE);
        //startService(intent);
    }*/

    // Khi Activity ngừng hoạt động.
    /*@Override
    protected void onStop()
    {
        super.onStop();
        // mServ.stopMusic();
        //doUnbindService();
        if (mIsBound) {
            // Hủy giàng buộc kết nối với dịch vụ.
            this.unbindService(Scon);
            mIsBound = false;
        }
    }*/
}
