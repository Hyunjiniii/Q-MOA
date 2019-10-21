package com.example.q_moa.favorites.Room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoriteRepository {
    public FavoriteDao mFavoriteDao;
    public LiveData<List<Favorite_Item>> mAllFavorite;

    FavoriteRepository(Application application){
        FavoriteDatabase db = FavoriteDatabase.getDatabase(application);
        mFavoriteDao = db.favoriteDao();
        mAllFavorite = mFavoriteDao.getAllFavorite();

    }

    public LiveData<List<Favorite_Item>> getmAllFavorite(){return mAllFavorite;}

    //insert
    public void insert(Favorite_Item word){
        new insertAsyncTask(mFavoriteDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Favorite_Item, Void, Void> {
        private FavoriteDao mAsyncTaskDao;

        insertAsyncTask(FavoriteDao dao){ mAsyncTaskDao = dao;}
        @Override
        protected Void doInBackground(Favorite_Item... words) {
            mAsyncTaskDao.insert(words[0]);
            return null;
        }
    }

    //delete
    public void delete(Favorite_Item word){
        new deleteAsyncTask(mFavoriteDao).execute(word);
    }

    private static class deleteAsyncTask extends AsyncTask<Favorite_Item, Void, Void>{
        private FavoriteDao mAsyncTaskDao;
        deleteAsyncTask(FavoriteDao dao) { mAsyncTaskDao = dao;}
        @Override
        protected Void doInBackground(Favorite_Item... words) {
            mAsyncTaskDao.delete(words[0]);
            return null;
        }
    }

}
