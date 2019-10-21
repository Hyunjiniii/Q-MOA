package com.example.q_moa.favorites.Room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    public FavoriteRepository mRepository;
    public LiveData<List<Favorite_Item>> mAllFavorite;


    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FavoriteRepository(application);
        mAllFavorite = mRepository.getmAllFavorite();
    }

    public LiveData<List<Favorite_Item>> getAllFavorite() {
        return mAllFavorite;
    }

    public void delete(Favorite_Item word) {
        mRepository.delete(word);
    }

    public void insert(Favorite_Item word) {
        mRepository.insert(word);
    }


}
