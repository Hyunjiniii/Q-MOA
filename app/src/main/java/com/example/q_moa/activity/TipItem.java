package com.example.q_moa.activity;

public class TipItem {
    private String nickname;
    private String contents;
    private String like_result;
    private String date;
    private String isLike;
    private String isUnLike;
    private String unlike_result;

    TipItem() {

    }

    TipItem(String nickname, String contents, String date, String like_result, String unlike_result) {
        this.nickname = nickname;
        this.contents = contents;
        this.like_result = like_result;
        this.unlike_result = unlike_result;
        this.date = date;
    }

    TipItem(String nickname, String contents, String date, String like_result, String unlike_result, String isLike, String isUnLike) {
        this.nickname = nickname;
        this.contents = contents;
        this.like_result = like_result;
        this.unlike_result = unlike_result;
        this.isLike = isLike;
        this.isUnLike = isUnLike;
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public String getContents() {
        return contents;
    }

    public String getDate() {
        return date;
    }

    public String getLike_result() {
        return like_result;
    }

    public String getUnlike_result() {
        return unlike_result;
    }

    public String getIsLike() {
        return isLike;
    }

    public String getIsUnLike() {
        return isUnLike;
    }
}
