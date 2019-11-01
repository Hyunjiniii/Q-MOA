package com.example.q_moa.favorites.Room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
            holder.text1.setText(current.getText1());
            holder.text2.setText(current.getText2());

        }
    }

    public Favorite_Item getWordAtPosition (int position) {
        return mfavorite.get(position);
    }

    public int getSize (){ return mfavorite.size();}

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
        TextView name, text1, text2, bottomtext;
        ImageView button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.favorite_time);
            text1 = itemView.findViewById(R.id.tv_text1);
            text2 = itemView.findViewById(R.id.tv_text2);
            bottomtext = itemView.findViewById(R.id.bottomtext);
            button = itemView.findViewById(R.id.unfavorite_btn);
          //  button.setImageResource(R.drawable.ic_grade_black_24dp);

        }
    }
}
