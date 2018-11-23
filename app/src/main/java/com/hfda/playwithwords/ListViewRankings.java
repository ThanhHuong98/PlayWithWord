package com.hfda.playwithwords;
//Class save data cua Item trong ListView: Stt, Name, Point
public class ListViewRankings
{
    private String sttRanking;
    private String userName;
    private  String userPoint;
    public ListViewRankings()
    {

    }
    public ListViewRankings(String _sttRankings, String _userName, String _userPoint)
    {
        sttRanking=_sttRankings;
        userName=_userName;
        userPoint=_userPoint;
    }
    public String getUserName()
    {
        return userName;
    }
    public void setUserName(String mUserName)
    {
        userName=mUserName;
    }
    public String getSttRankings()
    {
        return sttRanking;
    }
    public void setSttRanking(String mSttRankings)
    {
        sttRanking=mSttRankings;
    }
    public String getUserPoint()
    {
        return userPoint;
    }
    public void setUserPoint(String mUserPoint)
    {
        userPoint=mUserPoint;
    }
}
