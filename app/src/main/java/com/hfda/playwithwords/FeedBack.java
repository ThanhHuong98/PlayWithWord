package com.hfda.playwithwords;

public class FeedBack {
    private int numberStar;
    private  String text;

    public int getNumberStar() {
        return numberStar;
    }

    public void setNumberStar(int numberStar) {
        this.numberStar = numberStar;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public FeedBack()
    {

    }
    public FeedBack(int a,String b)
    {
        this.numberStar=a;
        this.text=b;
    }
}
