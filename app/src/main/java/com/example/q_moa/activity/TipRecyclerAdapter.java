package com.example.q_moa.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TipRecyclerAdapter extends RecyclerView.Adapter<TipRecyclerAdapter.MyViewHolder> {
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private List<TipItem> items;
    private Context context;
    private String uid = firebaseUser.getUid();
    private String sub_name;
    private String series;
    private String name;
    private String contents;
    private String date;
    private boolean like;
    private int result;

    TipRecyclerAdapter(List<TipItem> items, Context context, String sub_name, String series) {
        this.items = items;
        this.context = context;
        this.sub_name = sub_name;
        this.series = series;
    }

    @NonNull
    @Override
    public TipRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        TipItem item = items.get(holder.getAdapterPosition());

        name = item.getNickname();
        contents = item.getContents();
        date = item.getDate();

        holder.nickname.setText(name);
        holder.contents.setText(contents);

        firebaseDatabase.child("Review").child(sub_name).child(series).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String slike = dataSnapshot.child("like").getValue().toString();
                String sresult = dataSnapshot.child("result").getValue().toString();
                like = Boolean.parseBoolean(slike);
                result = Integer.parseInt(sresult);

                holder.like_button.setChecked(like);
                holder.tip_result.setText(sresult);

                like = holder.like_button.isChecked();
                holder.like_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TipItem item = items.get(holder.getAdapterPosition());
                        date = item.getDate();
                        if (like) {
                            result -= 1;
                            TipItem item1 = new TipItem(name, contents, String.valueOf(result), date, false);
                            firebaseDatabase.child("Review").child(sub_name).child(series).child(date).setValue(item1);
                            like = item1.isLike();
                            holder.like_button.setChecked(like);
                            holder.tip_result.setText(item1.getResult());
                        } else if (!like) {
                            result += 1;
                            TipItem item1 = new TipItem(name, contents, String.valueOf(result), date, true);
                            firebaseDatabase.child("Review").child(sub_name).child(series).child(date).setValue(item1);
                            like = item1.isLike();
                            holder.like_button.setChecked(like);
                            holder.tip_result.setText(item1.getResult());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nickname;
        TextView contents;
        TextView tip_result;
        ToggleButton like_button;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            nickname = (TextView) itemView.findViewById(R.id.tip_item_nickname);
            contents = (TextView) itemView.findViewById(R.id.tip_item_contents);
            tip_result = (TextView) itemView.findViewById(R.id.tip_result_button);
            like_button = (ToggleButton) itemView.findViewById(R.id.tip_like_button);

        }
    }

}
