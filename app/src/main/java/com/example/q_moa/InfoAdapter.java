package com.example.q_moa;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.q_moa.activity.InfoActivity;
import com.example.q_moa.favorites.Room.FavoriteListAdapter;
import com.example.q_moa.favorites.Room.FavoriteViewModel;
import com.example.q_moa.favorites.Room.Favorite_Item;
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

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    public LayoutInflater mInflater;
    public Context context;
    public List<Favorite_Item> mfavorite;
    FavoriteViewModel viewModel;
    private String uid;
    private String sub_name;
    private String series;

    public InfoAdapter(Context context, List<Favorite_Item> mfavorite, FavoriteViewModel viewModel, String uid, String sub_name, String series) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mfavorite = mfavorite;
        this.viewModel = viewModel;
        this.uid = uid;
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

        final Favorite_Item current = mfavorite.get(position);

        holder.time.setText("제 " + current.getTime());
        holder.text1.setText(current.getText1());
        holder.text2.setText(current.getText2());

    }


    public int getItemCount() {
        return mfavorite.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2, time;
        ImageView unfavorite_btn;
        boolean bo = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.tv_text1);
            text2 = itemView.findViewById(R.id.tv_text2);
            time = itemView.findViewById(R.id.favorite_time);
            unfavorite_btn = itemView.findViewById(R.id.unfavorite_btn);

            unfavorite_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        Favorite_Item item = mfavorite.get(pos);

                        if (uid != null && bo == false) {

                            Favorite_Item itemData = new Favorite_Item("[" + series + "] ", sub_name, item.getTime(), item.getText1(), item.getText2());

                            unfavorite_btn.setImageResource(R.drawable.favorite_ic);

                            viewModel.insert(itemData);

                            bo = true;

                        } else if (uid != null && bo == true) {

                            unfavorite_btn.setImageResource(R.drawable.unfavorite_ic);
                            Favorite_Item itemData = new Favorite_Item("[" + series + "] ", sub_name, item.getTime(), item.getText1(), item.getText2());

                            viewModel.delete(itemData);
                            bo = false;

                        } else
                            Toast.makeText(context, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();

                    } else {

                    }

                }
            });
        }
    }

//    private void setFavorite(String tiem) {
//        Hashtable<String, Boolean> star = new Hashtable<>();
//        star.put(uid, true);
//
//        firebaseDatabase.child("국가기술자격").child("기술").child(sub_name).child(series).child(tiem).child("star").setValue(star);
//    }
//
//    private void deletFavorite(String time) {
//        final Query query = firebaseDatabase.child("국가기술자격").child("기술").child(sub_name).child(series).child(time).child("star").child(uid);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                query.getRef().removeValue();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

}
