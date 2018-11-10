package com.hfda.playwithwords;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Round_Mode2 extends Fragment implements fromContainerToFrag, View.OnClickListener
{
    Button[] btnAnswer = new Button[4]; //mảng chứa 4 button đáp án
    Button userChosen;
    ImageButton btnHint;
    TextView textPoint;
    TextView numberRound;
    ImageView imgRound;
    TextView hint;
    ProgressBar myProgressBar;

    Round _container; //Activity chứa Fragment

    int numberHint=5; //số hint tối đa cho người dùng
    int point=0; //điểm người dùng có được

    int accum;
    int progressStep=1;
    Handler myHandler;
    Runnable runnable;

    String realAnswer;
    public Fragment_Round_Mode2()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        LinearLayout layout = (LinearLayout) inflater.inflate
                (R.layout.fragment_round_mode2, container, false);

        _container = (Round)getActivity();

        myProgressBar = layout.findViewById(R.id.progressbar);
        numberRound = (layout).findViewById(R.id.textViewRound);
        imgRound=(layout).findViewById(R.id.imgView);
        btnAnswer[0]=(layout).findViewById(R.id.btnAnswer1);
        btnAnswer[1]=(layout).findViewById(R.id.btnAnswer2);
        btnAnswer[2]=(layout).findViewById(R.id.btnAnswer3);
        btnAnswer[3]=(layout).findViewById(R.id.btnAnswer4);
        textPoint= (layout).findViewById(R.id.textViewPoint);
        btnHint=(layout).findViewById(R.id.btnHint);



        hint = layout.findViewById(R.id.textViewNumberHint);



        btnAnswer[0].setOnClickListener(this);
        btnAnswer[1].setOnClickListener(this);
        btnAnswer[2].setOnClickListener(this);
        btnAnswer[3].setOnClickListener(this);
        btnHint.setOnClickListener(this);

        //Khi mới vào vòng đầu tiên thì phải gửi thông điệp lên Activity Round ở trên để nó gửi
        //thông điệp + database xuống cho mình set dữ liệu trên màn hình chơi
        _container.Action("REFRESH");

        return layout;
    }


    public void StartProgressBar()
    {
        myHandler = new Handler();
        accum=0;
        myProgressBar.setMax(1000);
        myProgressBar.setProgress(1);

        myHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run()
            {
                if(accum<=myProgressBar.getMax())
                {
                    myProgressBar.incrementProgressBy(progressStep);
                    accum++;
                    myHandler.postDelayed(runnable, 10);
                }
                else
                    _container.Action("REFRESH");
            }
        };
        myHandler.post(runnable);
    }


    @Override
    public void onClick(View v)
    {
        if(v.getId()==btnHint.getId()) //người dùng bấm chọn hint
        {
            if(numberHint>0) //nếu còn trợ giúp
            {
                //lam mo di 1 dap an sai
                for (Button e : btnAnswer) //coi trong 4 đáp án, cái nào sai thì ẩn nó đi
                {
                    //nếu đáp án trong button là sai và button đó chưa bị ẩn
                    // (cái này để cho trường hợp người dùng sử dụng nhiều hint trong 1 vòng)
                    if (!e.getText().toString().equalsIgnoreCase(realAnswer) && e.isEnabled())
                    {
                        numberHint--;
                        e.setEnabled(false);
                        e.setVisibility(View.INVISIBLE);
                        break;
                    }
                }

                String text = "Hint: " + numberHint + "/5";
                hint.setText(text);
            }
        }
        else //nguoi dung chon dap an
        {
            userChosen=(Button)v;

            if(userChosen.getText().equals(realAnswer)) //đáp án người dùng chọn trùng với đáp án chính
            {
                //set background cho cái nút mà người dùng chọn thành màu xanh
                userChosen.setBackground(this.getResources().getDrawable(R.drawable.btn_right));
                //tăng điểm lên
                point+=20;

                String text = point + "";
                //setText cho cái TextView Point(s)
                textPoint.setText(text);

                //phát nhạc "ting ting" báo hiệu người dùng đã chọn đúng
                    //từ từ xử lý sau =))
                _container.Action("RIGHT");
            }
            else //đáp án người dùng chọn khác với đáp án chính
            {
                userChosen.setBackground(this.getResources().getDrawable(R.drawable.btn_wrong));
                _container.Action("WRONG");
                //phát nhạc báo hiệu sai
                    //cái này cũng từ từ xử lý sau =)
            }
            //sau khi người dùng đã click chọn đáp án thì sẽ cho dừng khoảng 1s để hiện nút xanh xanh báo đúng này nọ
            //chứ không cho dừng thì nó nhảy qua màn khác ngay lập tức, người ta sẽ không thấy báo hiệu đúng/sai gì cả
            //trong lúc dừng 1s đó, có thể người dùng sẽ bấm vào đáp án khác nữa hoặc là bấm vào hint
            //do đó mình phải unenable mấy cái nút đó hết, khi nào chuyển qua câu hỏi mới thì set enable nó lại, để tránh
            //người dùng bấm lung tung
            for(Button e:btnAnswer)
            {
                e.setEnabled(false);
            }
            btnHint.setEnabled(false);

            //cho progressbar dừng lại không chạy nữa
            myHandler.removeCallbacks(runnable);

            //gửi cái đáp án người dùng chọn lên Activity để nó chỉnh sửa thông tin điểm ở trển để cuối cùng nó gửi
            //mấy cái thông tin điểm, số màn chơi người dùng làm đúng qua cho Activity Result thông qua intent

            //cho dừng khoảng 0.6s sau đó gửi thông điệp lên Activity ở trên để chuyển qua câu hỏi mới
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    //sau 1s nó sẽ gửi thông điệp REFRESH lên trên Activity để lấy dữ liệu câu hỏi + đáp án mới
                    //về set lại trên màn hình
                    _container.Action("REFRESH");
                }
            }, 1000);

        }
    }
    /*
    phương thức message trong Interface fromContainerToFrag sẽ được kích hoạt khi mà Activity gửi dữ liệu xuống Fragment*/
    @Override
    public void InfoToHandle(String mess, String roundOfMode, Object question, Object answer, String transcription, String[] answerInBtn)
    {

        if(mess.equals("NEW")) //Activity gửi thông diệp xuống kêu set lại dữ liệu trên màn hình cho vòng chơi mới
        {

            String text= roundOfMode + "/20";
            numberRound.setText(text);

            //set lai cau hoi

           // textViewQuestion.setText((String)question); //phai ep kieu question tuy theo mode (String, Drawable, ...)

            //set lai phien am

          //  textViewTranscript.setText(transcription);
             imgRound.setImageResource((Integer) question);
             realAnswer = (String)answer;
            //set lại dữ liệu cho 4 button đáp án, nếu nó có bị unenable hoặc bị ẩn thì cho nó bình thường trở lại
            for(int i=0; i<4; i++)
            {
                if(!btnAnswer[i].isEnabled()) btnAnswer[i].setEnabled(true);
                if(!btnAnswer[i].isShown()) btnAnswer[i].setVisibility(View.VISIBLE);
                btnAnswer[i].setText(answerInBtn[i]);
            }
            //set cái background button đáp án người dùng chọ lúc nãy về bình thường, không còn xanh đỏ nữa
            if(userChosen!=null)
            {
                userChosen.setBackground(this.getResources().getDrawable(R.drawable.button_mode2));
            }

            if(!btnHint.isEnabled())
            {
                btnHint.setEnabled(true);
            }

            this.realAnswer =(String)answer; //luư đáp án mà Activity ở trên gửi về
            StartProgressBar();
        }
    }
}
