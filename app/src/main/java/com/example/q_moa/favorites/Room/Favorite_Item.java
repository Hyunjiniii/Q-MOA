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

    public String series;

    @ColumnInfo(name = "favorite_text1")
    public String favorite_text1;

    @ColumnInfo(name = "favortie_text2")
    public String favorite_text2;

    public Favorite_Item(@NonNull String favorite_name, String series, String favorite_text1, String favorite_text2) {
        this.favorite_name = favorite_name;
        this.series = series;
        this.favorite_text1 = favorite_text1;
        this.favorite_text2 = favorite_text2;
    }

    @NonNull
    public String getFavorite_name() {
        return favorite_name;
    }

    public String getSeries() {
        return series;
    }

    public String getFavorite_text1() {
        return favorite_text1;
    }

    public String getFavorite_text2() {
        return favorite_text2;
    }


}

