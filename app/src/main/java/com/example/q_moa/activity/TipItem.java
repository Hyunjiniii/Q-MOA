package com.example.q_moa.activity;

public class TipItem {
    private String nickname;
    private String contents;
    private String like_result;
    private String date;
    private boolean isLike;
    private boolean isUnLike;
    private String unlike_result;
    private String certificate;
    private String uid;

    TipItem() {

    }

    TipItem(boolean isLike, boolean isUnLike) {
        this.isLike = isLike;
        this.isUnLike = isUnLike;
    }

    TipItem(String nickname, String contents, String date, String certificate) {
        this.nickname = nickname;
        this.contents = contents;
        this.date = date;
        this.certificate = certificate;
    }

    TipItem(String nickname, String contents, String date, String like_result, String unlike_result, String uid) {
        this.nickname = nickname;
        this.contents = contents;
        this.like_result = like_result;
        this.unlike_result = unlike_result;
        this.date = date;
        this.uid = uid;
    }

    TipItem(String nickname, String contents, String date, String like_result, String unlike_result, boolean isLike, boolean isUnLike) {
        this.nickname = nickname;
        this.contents = contents;
        this.like_result = like_result;
        this.unlike_result = unlike_result;
        this.date = date;
        this.isLike = isLike;
        this.isUnLike = isUnLike;
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

    public String getCertificate() {
        return certificate;
    }

    public boolean isLike() {
        return isLike;
    }

    public boolean isUnLike() {
        return isUnLike;
    }

    public String getUid() {
        return uid;
    }
}
