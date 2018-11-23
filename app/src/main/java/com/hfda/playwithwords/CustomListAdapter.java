package com.hfda.playwithwords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

//Do tu custom 1 item trong ListView nen phai co Class CustomListAdapter nay
public class CustomListAdapter extends BaseAdapter
{
    private List<ListViewRankings> listViewRankingsList;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context context,List<ListViewRankings> listViewRankings)
    {
        this.context=context;
        this.listViewRankingsList=listViewRankings;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listViewRankingsList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewRankingsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout_ranking, null);
            holder = new ViewHolder();
            holder.sttRankings = (TextView) convertView.findViewById(R.id.tvSTT);
            holder.userName = (TextView) convertView.findViewById(R.id.userNamePlay);
            holder.userPoint = (TextView) convertView.findViewById(R.id.pointUserPlay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Set lai du lieu hien thi cho cac Items trong ListView
        ListViewRankings mListViewRankings=this.listViewRankingsList.get(position);
        holder.sttRankings.setText(mListViewRankings.getSttRankings());
        holder.userName.setText(mListViewRankings.getUserName());
        holder.userPoint.setText(mListViewRankings.getUserPoint());
        return convertView;
    }
    static class ViewHolder
    {
        TextView sttRankings;
        TextView userName;
        TextView userPoint;
    }
}
