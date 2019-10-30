package com.example.q_moa.favorites.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_word")
public class Favorite_Item {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "favorite_name")
    public String favorite_name;

    @ColumnInfo(name = "favorite_time")
    public String time;

    @ColumnInfo(name = "favorite_text1")
    public String text1;

    @ColumnInfo(name = "favortie_text2")
    public String text2;

    Favorite_Item() {

    }

    public Favorite_Item(@NonNull String favorite_time, String favorite_text1, String favorite_text2) {
        this.time = favorite_time;
        this.text1 = favorite_text1;
        this.text2 = favorite_text2;
    }

    public Favorite_Item(@NonNull String favorite_name, String series, String time, String favorite_text1, String favorite_text2) {
        this.favorite_name = "제 "+time+ ""+favorite_name+" "+series;
        this.time = time;
        this.text1 = favorite_text1;
        this.text2 = favorite_text2;
    }

    @NonNull
    public String getFavorite_name() {
        return favorite_name;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getTime() {
        return time;
    }

}

