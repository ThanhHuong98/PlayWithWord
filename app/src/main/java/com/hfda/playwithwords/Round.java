package com.hfda.playwithwords;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

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
    private Object newQuestion; //
    private Object newAnswer;
    private String newTransciption;
    private String[] newAnswerInBtn = new String[4];

    private ArrayList<Integer> idArray = new ArrayList<>(); //mảng chứa các dòng đã được truy xuất từ database trước đó
    //khi random để lấy 1 dòng mới thì phải so với mảng xem dòng đó đã được random chưa

    //hàm để xáo trộn mảng một cách ngẫu nhiên
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

    //Ham de doc du lieu tu data base len
    private void readData()
    {
        boolean same = true; // biến để kiểm tra xem có lấy id trùng với những cái trước đó?
        int number_rows = 0; //số lượng dòng trong bảng data
        try {

            SQLiteOpenHelper myDatabase = new MyDatabase(this);
            SQLiteDatabase db = myDatabase.getReadableDatabase();

            Cursor cursor = db.query("DATA",
                    new String[]{"COUNT(ID) AS NUM_ROW"},
                    null, null, null, null, null); //truy vấn số lượng dòng

             if(cursor.moveToFirst())//di chuyển con trỏ lên dòng đầu tiên của bảng kết quả
            {
                number_rows = cursor.getInt(0); //lấy dữ liệu ở cột thứ 0, kiểu int
            }
            Random rd=new Random();
          //  int randRow = 1+(int)Math.random()*(number_rows-1); //random ra 1 dòng dữ liệu
            int randRow=rd.nextInt(number_rows)+1;
            while(!idArray.isEmpty() && idArray.indexOf(randRow)!=-1)
            {
                randRow=rd.nextInt(number_rows)+1;
              //  randRow = 1+(int)Math.random()*(number_rows-1); //phải random lại dòng mới nếu random ra trùng với những câu đã lấy trước đó
            }
            idArray.add(randRow); //nếu không trùng thì thêm vào ArrayList

            //tiếp tục truy vấn để lấy bộ câu hỏi dựa theo id đã random
            cursor = db.query("DATA",
                    null,
                    "ID = ?",
                    new String[] {String.valueOf(randRow)}, //dựa vào số dòng mà random ra id
                    null, null, null);

            if(cursor.moveToFirst()) //di chuyển con trỏ lên đầu bảng kết quả
            {
                switch(mode) //lấy dữ liệu bộ câu hỏi + đáp án tùy theo từng mode
                {
                    case 1:
                    {
                        newQuestion = cursor.getString(1);
                        newTransciption = cursor.getString(2);
                        newAnswer = cursor.getString(3);

                        Log.d("aaa",newTransciption);
                        ArrayList<Integer> rand = new ArrayList<>(); // chứa id của dòng chứa câu hỏi và 3 câu trả lời sai
                        rand.add(randRow);
                        //chạy vòng lặp để lấy ngẫu nhiên 3 từ tiếng Việt để đặt trong 3 btn đáp án sai
                        newAnswerInBtn[0] = (String)newAnswer;
                        for(int i=1; i<=3; i++)
                        {
                           // int random = 1+(int)Math.random()*(number_rows-1);
                            int random=rd.nextInt(number_rows)+1;
                            while(rand.indexOf(random)!=-1)
                            {
                                random = rd.nextInt(number_rows)+1;
                            }

                            rand.add(random);

                            cursor = db.query("DATA",
                                    new String[] {"VN_TEXT"},
                                    "ID = ?",
                                    new String[] {String.valueOf(random)},
                                    null, null, null); //truy xuất từ bảng DATA
                            if(cursor.moveToFirst())
                            {
                                newAnswerInBtn[i] = cursor.getString(0);
                            }

                        }
                        SufferStringArray(newAnswerInBtn); //hoán vị vị trí của các đáp án
                        break;
                    }
                    case 2:
                    {
                        newQuestion = cursor.getInt(5); //câu hỏi là hình ảnh
                        newAnswer = cursor.getString(1); // đáp án là từ tiếng anh

                        ArrayList<Integer> rand = new ArrayList<>(); // chứa id của dòng chứa câu hỏi và 3 câu trả lời sai
                        rand.add(randRow);
                        newAnswerInBtn[0] = (String)newAnswer;
                        //chạy vòng lặp để lấy ngẫu nhiên 3 từ tiếng Anh nữa để đặt vào mảng btn đáp án
                        for(int i=1; i<=3; i++)
                        {
                            int random = rd.nextInt(number_rows)+1;

                            while(rand.indexOf(random)!=-1)
                            {
                                random = rd.nextInt(number_rows)+1;
                            }

                            rand.add(random);


                            cursor = db.query("DATA",
                                    new String[] {"ENGLISH_TEXT"},
                                    "ID = ?",
                                    new String[] {String.valueOf(random)},
                                    null, null, null); //truy xuất từ bảng DATA
                           if(cursor.moveToFirst())

                           {
                               newAnswerInBtn[i] = cursor.getString(0);
                           }
                        }
                        SufferStringArray(newAnswerInBtn); //hoán vị vị trí của các đáp án
                        break;
                    }
                    case 3:
                    {
                        newQuestion = cursor.getString(4);
                        newAnswer = cursor.getString(1);
                        break;
                    }
                    case 4:
                    {
                        newQuestion = cursor.getInt(6);
                        newAnswer = cursor.getString(1);
                        break;
                    }
                    case 6:
                    {
                        newQuestion = cursor.getString(1);
                        newAnswer = cursor.getInt(6);
                        break;
                    }
                }
                cursor.close();
                db.close();
            }
        } catch(SQLException ex) {
            Log.e("Error", "An error occured when trying to access database!");
        }

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
                readData();
                round++;
                //thay đổi tất cả text trong button và TextView câu hỏi thành 1 text khác
                switch (mode)
                {
                    case 1:
                        //Activity Round sẽ gửi bộ dữ liệu gồm câu hỏi và 4 đáp án mới xuống cho Fragment set lại
                        //thông qua phương thức message của Interface fromContainerToFrag
                        fragmentRound1.InfoToHandle("NEW", round+"", newQuestion, newAnswer, newTransciption, newAnswerInBtn);
                        break;
                    case 2:
                        fragmentRound2.InfoToHandle("NEW", round+"",newQuestion,newAnswer,"",newAnswerInBtn);
                        break;
                    case 3:
                        fragmentRound3.InfoToHandle("NEW", round+"", newQuestion, newAnswer, "", null);
                        break;
                    case 4:
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
                    // Hide your View after 1 seconds
                    gifImageView.setVisibility(View.INVISIBLE);//VISIBLE, GONE
                }
            }, 1000);
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

        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, Menu.class));
        finish();
    }
   //@Override
   // protected void onStart()
  // {
       //super.onStart();
        // Tạo đối tượng Intent cho WeatherService.
      // Intent intent = new Intent(Round.this,MusicService.class);

        // Gọi method bindService(..) để giàng buộc dịch vụ với giao diện.
     //   this.bindService(intent, Scon, Context.BIND_AUTO_CREATE);
        //startService(intent);
   // }

    // Khi Activity ngừng hoạt động.
    @Override
    protected void onStop()
    {
        super.onStop();
        // mServ.stopMusic();
        //doUnbindService();
        if (mIsBound) {
            // Hủy ràng buộc kết nối với dịch vụ.
            this.unbindService(Scon);
            mIsBound = false;
        }
    }
}
