package com.hfda.playwithwords;

public class Mode5 {
    private  String VN;//Câu tiếng việt
    private  String TA;//Câu tiếng anh
    public Mode5()
    {
        //mac dinh
    }
    public Mode5(String a, String b)
    {
        VN=a;
        TA=b;
    }

    public String getVN() {
        return VN;
    }

    public void setVN(String VN) {
        this.VN = VN;
    }

    public String getTA() {
        return TA;
    }

    public void setTA(String TA) {
        this.TA = TA;
    }
}
