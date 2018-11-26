package com.hfda.playwithwords;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRankings extends Fragment
{

    ListView lv;
    List<ListViewRankings>mListViewRankings;
    public FragmentRankings() {
        // Required empty public constructor
    }

    protected static int userRanking(String name) //thứ tự của user trong bảng xếp hạng
    {
        int rank=1;
        for(int i=0; i < MainActivity.mUser.size(); i++)
        {
            if(MainActivity.mUser.get(i).getName().equals(name))
            {
                for(int j=0; j<i; j++)
                {
                    if(MainActivity.mUser.get(j).getTotalScore() > MainActivity.mUser.get(i).getTotalScore())
                        rank++;
                }
            }
        }
        return rank;
    }
    //Sắp xếp các tài khoản người dùng theo thứ tự từ cao đến thấp để xếp hạng
    protected static void SortUserList(List<User> array)
    {
        for(int i=0; i< array.size()-1; i++)
        {
            for(int j=i+1; j<array.size(); j++)
            {
                if(array.get(i).getTotalScore() < array.get(j).getTotalScore())
                {
                    User temp = array.get(i);
                    array.set(i, array.get(j));
                    array.set(j, temp);
                }
            }
        }
    }
    private void setRankChart(View view, List<User> array)
    {
        TextView userNameFirst = view.findViewById(R.id.userHang1);
        TextView userNameSecond = view.findViewById(R.id.userHang2);
        TextView userNameThird = view.findViewById(R.id.userHang3);

        TextView userScoreFirst = view.findViewById(R.id.pointHang1);
        TextView userScoreSecond = view.findViewById(R.id.pointHang2);
        TextView userScoreThird = view.findViewById(R.id.pointHang3);

        User top3[] = new User[3];
        for(int i=0; i< 3; i++)
        {
            top3[i] = array.get(i);
            if(i==0)
            {
                if(top3[i]!=null)
                {
                    userNameFirst.setText(top3[i].getName());
                    userScoreFirst.setText(String.valueOf(top3[i].getTotalScore()));
                }
                else
                {
                    userNameFirst.setText("...");
                    userScoreFirst.setText("...");
                }
            }
            else
            {
                if(top3[i]!=null)
                {
                    if(i==1)
                    {
                        userNameSecond.setText(top3[i].getName());
                        userScoreSecond.setText(String.valueOf(top3[i].getTotalScore()));
                    }
                    if(i==2)
                    {
                        userNameThird.setText(top3[i].getName());
                        userScoreThird.setText(String.valueOf(top3[i].getTotalScore()));
                    }
                    if(top3[i-1]!=null && top3[i-1].getTotalScore()==top3[i].getTotalScore())
                    {
                        //set độ cao của cột bằng với thằng đồng hạng
                        RelativeLayout chartRank = null;
                        TextView textRank= null;
                        RelativeLayout chartRankPre = null;
                        if(i==1)
                        {
                            chartRank = view.findViewById(R.id.point_rankSecond);
                            chartRankPre = view.findViewById(R.id.point_rankFirst);
                            textRank = view.findViewById(R.id.rank1);
                        }
                        else if(i==2)
                        {
                            chartRank = view.findViewById(R.id.point_rankThird);
                            chartRankPre = view.findViewById(R.id.point_rankSecond);
                            textRank = view.findViewById(R.id.rank2);
                        }

                        //lấy chiều cao và kích thước text của cột hạng trước nó
                       // chartRank.getLayoutParams().height = chartRankPre.getHeight();
                        //set lại số thứ tự rank
                        textRank.setText(userRanking(top3[i].getName())+"");
                    }
                }
                else
                {
                    if(i==1)
                    {
                        userNameSecond.setText("...");
                        userScoreSecond.setText("...");
                    }
                    if(i==2)
                    {
                        userNameThird.setText("...");
                        userScoreThird.setText("...");
                    }
                }
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.ranking_fragment, container, false);
        MainActivity.readUserInfo();
        List<User> arrayUser = MainActivity.mUser;
        SortUserList(arrayUser);
        setRankChart(view, arrayUser);

        mListViewRankings=getListData();
        lv = view.findViewById(R.id.lvRankings);
        lv.setAdapter(new CustomListAdapter(getContext(),mListViewRankings));
        lv.smoothScrollToPosition(3);//Cuon 3 Item keo
        return view;
    }
    private  List<ListViewRankings> getListData()
    {
        List<ListViewRankings> list = new ArrayList<>();
        //Cho nay doc du lieu cua 1 Item tren firebase, dang set data gia
        int size = MainActivity.mUser.size();
        if(size<3) //không còn ai để set danh sách phía dưới
        {
            for(int i=0; i<3; i++)
            {
                User a =MainActivity.mUser.get(i);
                ListViewRankings item = new ListViewRankings("...", "...", "...");
                list.add(item);
            }
            return list;
        }
        if(size>=10)
            size =10;
        for(int i=3; i<size; i++)
        {
            User a =MainActivity.mUser.get(i);
            ListViewRankings item = new ListViewRankings(userRanking(a.getName())+"", a.getName(), a.getTotalScore()+"");
            list.add(item);
        }
        return list;
    }

}
