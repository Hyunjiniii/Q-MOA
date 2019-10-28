package com.example.q_moa.favorites.Room;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.q_moa.R;
import com.example.q_moa.activity.InfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

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
            name = itemView.findViewById(R.id.favorite_time);
            text1 = itemView.findViewById(R.id.tv_text1);
            text2 = itemView.findViewById(R.id.tv_text2);
//            btn_favorite = itemView.findViewById(R.id.favorite);
        }
    }
}
