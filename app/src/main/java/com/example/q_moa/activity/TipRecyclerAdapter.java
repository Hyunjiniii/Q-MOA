package com.example.q_moa.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private String uid;
    private String sub_name;
    private String series;
    private int mlike_result;

    private boolean islike;

    TipRecyclerAdapter(List<TipItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final TipItem item = items.get(position);

        String time = item.getDate();
        int idx = time.indexOf(" ");
        final String time1 = time.substring(0, idx);

        if (sub_name == null) {
            holder.certificate.setVisibility(View.VISIBLE);
            holder.like_result.setVisibility(View.GONE);
            holder.like_off_button.setVisibility(View.GONE);
            holder.certificate.setText(item.getCertificate());
            holder.nickname.setText(item.getNickname());
            holder.contents.setText(item.getContents());
            holder.date.setText(time1);
        } else {
            holder.nickname.setText(item.getNickname());
            holder.date.setText(time1);
            holder.contents.setText(item.getContents());
            holder.like_result.setText(item.getLike_result());

            Log.d("like_result", String.valueOf(mlike_result));

            setButton(holder);

            if (uid != null) {
                firebaseDatabase.child("UserReviewLike").child(sub_name).child(series).child(item.getDate()).child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            islike = (boolean) dataSnapshot.child("like").getValue();
                            if (islike) {
                                setLikeOn(holder);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.like_off_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int mlike_result = Integer.parseInt(item.getLike_result());
                        mlike_result += 1;
                        Log.d("like_result", String.valueOf(mlike_result));
                        setLikeOn(holder);
                        holder.like_result.setText(String.valueOf(mlike_result));
                        TipItem item1 = new TipItem(true);
                        TipItem tipItem1 = new TipItem(item.getNickname(), item.getContents(), item.getDate(), String.valueOf(mlike_result), uid);
                        firebaseDatabase.child("UserReviewLike").child(sub_name).child(series).child(item.getDate()).child(uid).setValue(item1);
                        firebaseDatabase.child("UserReview").child(sub_name).child(series).child(item.getDate()).setValue(tipItem1);
                    }
                });
                holder.like_on_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "이미 좋아요를 누르셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                holder.like_off_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView certificate;
        TextView nickname;
        TextView contents;
        TextView like_result;
        TextView date;
        ImageView like_on_button;
        ImageView like_off_button;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            certificate = (TextView) itemView.findViewById(R.id.mytip_certificate_name);
            nickname = (TextView) itemView.findViewById(R.id.tip_item_nickname);
            contents = (TextView) itemView.findViewById(R.id.tip_item_contents);
            like_result = (TextView) itemView.findViewById(R.id.tip_like_result);
            date = (TextView) itemView.findViewById(R.id.tip_item_date);
            like_on_button = (ImageView) itemView.findViewById(R.id.tip_like_on_button);
            like_off_button = (ImageView) itemView.findViewById(R.id.tip_like_off_button);
        }
    }

    private void setButton(final MyViewHolder holder) {
        if (firebaseUser != null)
            uid = firebaseUser.getUid();

        if (uid == null) {
            holder.like_off_button.setEnabled(false);
        }
    }

    private void setLikeOn(MyViewHolder holder) {
        holder.like_on_button.setVisibility(View.VISIBLE);
        holder.like_off_button.setVisibility(View.GONE);
    }


}
