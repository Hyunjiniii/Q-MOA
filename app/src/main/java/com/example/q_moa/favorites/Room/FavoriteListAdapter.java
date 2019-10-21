package com.example.q_moa.favorites.Room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.q_moa.R;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {
    public LayoutInflater mInflater;
    public List<Favorite_Item> mfavorite;
    Context context;
    FavoriteViewModel viewModel;

    public FavoriteListAdapter(Context context, FavoriteViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.favorite_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (mfavorite != null) {
            Favorite_Item current = mfavorite.get(position);
            holder.name.setText(current.getFavorite_name());

        }
    }

    public Favorite_Item getWordAtPosition (int position) {
        return mfavorite.get(position);
    }

    @Override
    public int getItemCount() {
        if (mfavorite != null)
            return mfavorite.size();
        else return 0;
    }

    public void setWords(List<Favorite_Item> words) {
        mfavorite = words;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, text1, text2;
        ImageButton btn_favorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            text1 = itemView.findViewById(R.id.tv_text1);
            text2 = itemView.findViewById(R.id.tv_text2);
            btn_favorite = itemView.findViewById(R.id.favorite);
        }
    }
}