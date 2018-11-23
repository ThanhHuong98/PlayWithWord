package com.hfda.playwithwords;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRankings extends Fragment {

    ListView lv;
    List<ListViewRankings>mListViewRankings;
    public FragmentRankings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.ranking_fragment, container, false);
        mListViewRankings=getListData();
        lv = view.findViewById(R.id.lvRankings);
        lv.setAdapter(new CustomListAdapter(getContext(),mListViewRankings));

        lv.smoothScrollToPosition(3);//Cuon 3 Item keo

        return view;
    }
    private  List<ListViewRankings> getListData()
    {
        List<ListViewRankings> list = new ArrayList<ListViewRankings>();
        //Cho nay doc du lieu cua 1 Item tren firebase, dang set data gia
        ListViewRankings iTem1 = new ListViewRankings("4", "Tiger", "300");
        list.add(iTem1);
        ListViewRankings iTem2 = new ListViewRankings("5", "Owl", "250");
        list.add(iTem2);
        ListViewRankings iTem3 = new ListViewRankings("6", "Eeyore", "200");
        list.add(iTem3);
        ListViewRankings iTem4 = new ListViewRankings("7", "Rabbit", "150");
        list.add(iTem4);
        ListViewRankings iTem5 = new ListViewRankings("8", "Kanga", "100");
        list.add(iTem5);
        ListViewRankings iTem6 = new ListViewRankings("9", "Tiger", "70");
        list.add(iTem6);
        ListViewRankings iTem7 = new ListViewRankings("10", "Owl", "50");
        list.add(iTem7);

        return list;
    }

}
