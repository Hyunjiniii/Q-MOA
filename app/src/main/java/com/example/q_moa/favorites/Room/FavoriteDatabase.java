package com.example.q_moa.favorites.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Favorite_Item.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase{
    public abstract FavoriteDao favoriteDao();

    private static FavoriteDatabase INSTANCE;

    public static FavoriteDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
