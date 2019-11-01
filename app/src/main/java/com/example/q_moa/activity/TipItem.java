package com.example.q_moa.activity;

public class TipItem {
    private String nickname;
    private String contents;
    private String like_result;
    private String date;
    private boolean isLike;
    private String certificate;
    private String uid;

    TipItem() {

    }

    TipItem(boolean isLike) {
        this.isLike = isLike;
    }

    TipItem(String nickname, String contents, String date, String certificate) {
        this.nickname = nickname;
        this.contents = contents;
        this.date = date;
        this.certificate = certificate;
    }

    TipItem(String nickname, String contents, String date, String like_result, String uid) {
        this.nickname = nickname;
        this.contents = contents;
        this.like_result = like_result;
        this.date = date;
        this.uid = uid;
    }

    TipItem(String nickname, String contents, String date, String like_result, boolean isLike) {
        this.nickname = nickname;
        this.contents = contents;
        this.like_result = like_result;
        this.date = date;
        this.isLike = isLike;
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


    public String getCertificate() {
        return certificate;
    }

    public boolean isLike() {
        return isLike;
    }

    public String getUid() {
        return uid;
    }
}
