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
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    public LayoutInflater mInflater;
    public List<Favorite_Item> mfavorite;
    Context context;
    FavoriteViewModel viewModel;
    private String uid;
    private String sub_name;
    private String series;

    public FavoriteListAdapter(Context context, FavoriteViewModel viewModel, List<Favorite_Item> mfavorite) {
        this.context = context;
        this.viewModel = viewModel;
        this.mfavorite = mfavorite;
    }

    public FavoriteListAdapter(List<Favorite_Item> mfavorite, Context context, FavoriteViewModel viewModel, String sub_name, String series) {
        mInflater = LayoutInflater.from(context);
        this.mfavorite = mfavorite;
        this.context = context;
        this.viewModel = viewModel;
        this.sub_name = sub_name;
        this.series = series;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.favorite_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
//            BooleanFavorite(holder);
        }
        final Favorite_Item current = mfavorite.get(position);

        holder.time.setText("제 " + current.getTime());
        holder.text1.setText(current.getText1());
        holder.text2.setText(current.getText2());

        if (current.getFavorite_name() != null) {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(current.getFavorite_name());
        } else {
            holder.name.setVisibility(View.GONE);
        }

        holder.favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid != null) {
                    holder.unfavorite_btn.setVisibility(View.VISIBLE);
                    holder.favorite_btn.setVisibility(View.GONE);
                    deletFavorite(current.getTime());
                } else
                    Toast.makeText(context, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.unfavorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid != null) {
                    Favorite_Item item = new Favorite_Item("[" + series + "] " + sub_name, current.getTime(), current.getText1(), current.getText2());
                    holder.unfavorite_btn.setVisibility(View.GONE);
                    holder.favorite_btn.setVisibility(View.VISIBLE);
                    firebaseDatabase.child("UserFavorite").child(uid).child(series + sub_name + current.getTime()).setValue(item);
                    viewModel.insert(item);
                } else
                    Toast.makeText(context, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public Favorite_Item getWordAtPosition(int position) {
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
        TextView name, text1, text2, time;
        ImageView favorite_btn, unfavorite_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            text1 = itemView.findViewById(R.id.tv_text1);
            text2 = itemView.findViewById(R.id.tv_text2);
            time = itemView.findViewById(R.id.favorite_time);
            favorite_btn = itemView.findViewById(R.id.favorite_btn);
            unfavorite_btn = itemView.findViewById(R.id.unfavorite_btn);
        }
    }

    private void BooleanFavorite(final ViewHolder holder) {
        firebaseDatabase.child("국가기술자격").child("기술").child(sub_name).child(series).child("star").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    boolean star = (boolean) dataSnapshot.getValue();

                    if (star == true) {
                        holder.favorite_btn.setVisibility(View.VISIBLE);
                        holder.unfavorite_btn.setVisibility(View.GONE);
                    } else {
                        holder.favorite_btn.setVisibility(View.GONE);
                        holder.unfavorite_btn.setVisibility(View.VISIBLE);
                    }
                } catch (NullPointerException e) {
                    holder.favorite_btn.setVisibility(View.GONE);
                    holder.unfavorite_btn.setVisibility(View.VISIBLE);

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    private void setFavorite() {
//        Hashtable<String, Boolean> star = new Hashtable<>();
//        star.put(uid, true);
//
//        firebaseDatabase.child("UserFavorite").child(uid).child(series + sub_name + time).setValue(star);
//    }

    private void deletFavorite(String time) {
        final Query query = firebaseDatabase.child("UserFavorite").child(uid).child(series + sub_name + time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                query.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
