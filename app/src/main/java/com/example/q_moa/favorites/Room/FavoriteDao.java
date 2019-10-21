package com.example.q_moa.favorites.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("SELECT * From favorite_word")
    LiveData<List<Favorite_Item>> getAllFavorite();

    @Insert
    void insert(Favorite_Item favorite_item);

    @Delete
    void delete(Favorite_Item favorite_item);

}
