package com.hfda.playwithwords;

public class User {
    String name;
    String password;
    long totalScore;

    public User() {
    }
    public User(String name, String password, long score)
    {
        this.name = name;
        this.password = password;
        this.totalScore = score;
    }
    public String getName() {
        return name;
    }

    public void setName(String nameUser) {
        this.name = nameUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passWord) {
        this.password = passWord;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long point) {
        this.totalScore = point;
    }
}
