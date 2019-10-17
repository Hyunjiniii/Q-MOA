package com.example.q_moa.activity;

public class TipItem {
    private String nickname;
    private String contents;
    private String result;
    private String date;
    private boolean isLike;

    TipItem() {

    }

    TipItem(String nickname, String contents, String result, String date) {
        this.nickname = nickname;
        this.contents = contents;
        this.result = result;
        this.date = date;
    }

    TipItem(String nickname, String contents, String result, String date, boolean isLike) {
        this.nickname = nickname;
        this.contents = contents;
        this.result = result;
        this.date = date;
        this.isLike = isLike;
    }

    public String getNickname() {
        return nickname;
    }

    public String getContents() {
        return contents;
    }

    public String getResult() {
        return result;
    }

    public String getDate() {
        return date;
    }

    public boolean isLike() {
        return isLike;
    }
}
