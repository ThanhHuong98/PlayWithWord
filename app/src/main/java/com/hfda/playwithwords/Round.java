package com.hfda.playwithwords;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Round extends AppCompatActivity implements fromFragToContainer
{
    static boolean isStart=true;
    static boolean isStop=true;
    static final String MODE = "mode"; //Extra của intent, để nhận thông tin mode thứ mấy từ intent của Menu gửi qua
    static  int flag=0;
    private Fragment_Round_Mode1 fragmentRound1;
    private Fragment_Round_Mode2 fragmentRound2;
    private Fragment_Round_Mode3 fragmentRound3;
    private Fragment_Round_Mode4 fragmentRound4;
    private Fragment_Round_Mode5 fragmentRound5;
    private Fragment_Round_Mode6 fragmentRound6;
    private FragmentTransaction ft;
    private int mode;
    private int round = 0;
    private int point = 0;
    private int numberRightAnswer=0; //s&#x1ed1; c&acirc;u tr&#x1ea3; l&#x1edd;i &#x111;&uacute;ng
    private GifImageView gifImageView;
    private SoundManager notification = new SoundManager();


    //những thông tin cần set mới cho mỗi màn chơi trong 20 màn, tùy theo mỗi mode sẽ bỏ thêm
    //một vài đối tượng ở đây, vd như id hình ảnh, id âm thanh, chuỗi định nghĩa từ, ...
    //những đối tượng này sẽ được đọc ra từ database
    private String newQuestion; //
    private String newAnswer;
    private String newTransciption;
    private String[] newAnswerInBtn = new String[4];
    int[] dd=new int[30];
    private ArrayList<Integer> idArray = new ArrayList<>(); //mảng chứa các dòng đã được truy xuất từ database trước đó
    //khi random để lấy 1 dòng mới thì phải so với mảng xem dòng đó đã được random chưa
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        gifImageView=(GifImageView)findViewById(R.id.GifImageView);


        Intent intent = getIntent(); //nhận intent từ Menu gửi qua
        String m = intent.getStringExtra(MODE); //lấy Extra
        mode = Integer.parseInt(m); //chuyển Extra từ String sang int
        for(int i=0;i<dd.length;i++) dd[i]=0;
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
                fragmentRound2 = new Fragment_Round_Mode2();
                ft.replace(R.id.fragment_round, fragmentRound2);
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
                break;
            case 6:
                fragmentRound6 = new Fragment_Round_Mode6();
                ft.replace(R.id.fragment_round, fragmentRound6);
                break;
        }
        ft.commit();

    }
    public static void HienThongBaoMuaHint(Context context)
    {
        isStart=false;
        Button btnOK;
        CheckBox checkbox;
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_thongbaohethint);
        btnOK=(Button)dialog.findViewById(R.id.btnOK);
        checkbox=(CheckBox)dialog.findViewById(R.id.checkBox);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isStart=true;
            }
        });
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Round.flag=1;
                isStart=true;
            }
        });
        dialog.show();
    }

    //Khi một item ở bên dưới Fragment_Round_ModeX được click thì interface này sẽ được kích hoạt
    //và nhận thông tin ở dưới Fragment gửi lên
    @Override
    //action là hành động mà Fragment gửi lên
    public void Action(String action)
    {
        if(action.equals("REFRESH")) //người dùng đã click chọn/điền đáp án, ta sẽ set lại toàn bộ dữ liệu mới cho màn hình
        {
            Random rd=new Random();
            int index=rd.nextInt(MainActivity.mData.size());
            while(true)
            {
                if(dd[index]==1)
                {
                    index=rd.nextInt(MainActivity.mData.size());
                }
                else
                {
                    dd[index]=1;
                    break;
                }
            }
            if (round < 20) //nếu còn trong 20 vòng thì mới set dữ liệu lại
            {

                round++;
                //thay đổi tất cả text trong button và TextView câu hỏi thành 1 text khác
                switch (mode)
                {
                    case 1:
                        newQuestion=MainActivity.mData.get(index).getWordE();
                        newAnswer= MainActivity.mData.get(index).getWordV();
                        newTransciption=MainActivity.mData.get(index).getPronunciation();
                        ArrayList<Integer> rand = new ArrayList<>(); // chứa id của dòng chứa câu hỏi và 3 câu trả lời sai
                        rand.add(index);
                        newAnswerInBtn[0]=newAnswer;
                        for(int i=1; i<=3; i++)
                        {
                            int random = rd.nextInt(MainActivity.mData.size());
                            while(rand.indexOf(random)>=0)
                            {
                                random = rd.nextInt(MainActivity.mData.size());
                            }
                            rand.add(random);
                            newAnswerInBtn[i] = MainActivity.mData.get(random).getWordV();
                        }

                        fragmentRound1.InfoToHandle("NEW", round+"",newAnswer,newQuestion,newTransciption,"",newAnswerInBtn);
                        break;
                    case 2:
                        newQuestion=MainActivity.mData.get(index).getImage();
                        newAnswer= MainActivity.mData.get(index).getWordE();
                        ArrayList<Integer> rand1 = new ArrayList<>(); // chứa id của dòng chứa câu hỏi và 3 câu trả lời sai
                        rand1.add(index);
                        newAnswerInBtn[0]=newAnswer;
                        for(int i=1; i<=3; i++)
                        {
                            int random = rd.nextInt(MainActivity.mData.size());
                            while(rand1.indexOf(random)>=0)
                            {
                                random = rd.nextInt(MainActivity.mData.size());
                            }
                            rand1.add(random);
                            newAnswerInBtn[i] = MainActivity.mData.get(random).getWordE();
                        }
                        fragmentRound2.InfoToHandle("NEW", round+"",newAnswer,newQuestion,newTransciption,"",newAnswerInBtn);
                        break;
                    case 3:
                        newQuestion=MainActivity.mData.get(index).getDefinition();
                        newAnswer=MainActivity.mData.get(index).getWordE();
                        fragmentRound3.InfoToHandle("NEW", round+"",newAnswer,newQuestion,"","",null);
                        break;
                    case 4:
                        //Doc data o tung node tren cay JSON, gui xuong cho th Fragment xu ly, phat am thanh, so sanh đáp án ,,v, v
                        newQuestion=MainActivity.mData.get(index).getSound();
                        newAnswer=MainActivity.mData.get(index).getWordE();
                        fragmentRound4.InfoToHandle("NEW", round+"",newAnswer,newQuestion,"","",null);
                        break;
                    case 5:
                        fragmentRound5.InfoToHandle("NEW", round+"",newAnswer,newQuestion,newTransciption,"",newAnswerInBtn);
                        break;
                    case 6:
                        fragmentRound6.InfoToHandle("NEW", round+"",newAnswer,newQuestion,newTransciption,"",newAnswerInBtn);
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
                finish();
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
                    // Hide your View after 1 seconds
                    gifImageView.setVisibility(View.INVISIBLE);//VISIBLE, GONE
                }
            }, 1000);
            //Phát âm thanh câu trả lời đúng
            MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.sound_correct);
            mediaPlayer.start();
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
            }, 1000);
            //Phát âm thanh câu trả lời sai
            MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.sound_incorrect);
            mediaPlayer.start();

        }
        //Dùng cho mode 4, trường hợp nhập câu trả lời rỗng, thì xuất thông báo
        if(action.equals("NOTIFICATION"))
        {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup)findViewById(R.id.custom_toast_layout_id));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("You must write down at least one character...");

            Toast toast = new Toast(getApplicationContext());
          //  toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);//Vị trị toast
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, Menu.class));
        finish();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        finish();
    }
}
