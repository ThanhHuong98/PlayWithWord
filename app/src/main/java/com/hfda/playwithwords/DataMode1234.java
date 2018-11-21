package com.hfda.playwithwords;

public class DataMode1234
{
    private  String wordE;//từ tiếng anh
    private  String image;//Hình ảnh mode 2
    private  String sound;//âm thanh mode4
    private  String wordV;//nghĩa tiếng việt
    private  String pronunciation;//phiên dịch
    private  String definition;//Định nghĩa mode3

    public DataMode1234(String wordE, String image, String sound, String wordV, String pronunciation, String definition) {
        this.wordE = wordE;
        this.image = image;
        this.sound = sound;
        this.wordV = wordV;
        this.pronunciation = pronunciation;
        this.definition = definition;
    }


    public  DataMode1234()
    {
        //constructor mac dinh
    }

    public String getWordE() {
        return wordE;
    }

    public void setWordE(String wordE) {
        this.wordE = wordE;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getWordV() {
        return wordV;
    }

    public void setWordV(String wordV) {
        this.wordV = wordV;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
