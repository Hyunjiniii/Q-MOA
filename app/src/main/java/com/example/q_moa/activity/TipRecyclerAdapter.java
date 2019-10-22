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
    private String uid;
    private String sub_name;
    private String series;
    private int mlike_result;
    private int munlike_result;

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
            holder.like_button.setVisibility(View.GONE);
            holder.like_result.setVisibility(View.GONE);
            holder.unlike_button.setVisibility(View.GONE);
            holder.unlike_result.setVisibility(View.GONE);
            holder.certificate.setText(item.getCertificate());
            holder.nickname.setText(item.getNickname());
            holder.contents.setText(item.getContents());
            holder.date.setText(time1);
            Log.d("Called", "MyTip");
        } else {
            holder.nickname.setText(item.getNickname());
            holder.date.setText(time1);
            holder.contents.setText(item.getContents());
            holder.like_result.setText(item.getLike_result());
            holder.unlike_result.setText(item.getUnlike_result());
            holder.like_button.setChecked(Boolean.parseBoolean(item.getIsLike()));
            holder.unlike_button.setChecked(Boolean.parseBoolean(item.getIsUnLike()));

            mlike_result = Integer.parseInt(item.getLike_result());
            munlike_result = Integer.parseInt(item.getUnlike_result());

            if (firebaseUser != null)
                uid = firebaseUser.getUid();

            if (uid == null) {
                holder.unlike_button.setEnabled(false);
                holder.like_button.setEnabled(false);
            }

            setButton(holder, item);

            if (uid != null) {
                holder.like_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mlike_result += 1;
                        holder.like_button.setChecked(true);
                        holder.like_result.setText(String.valueOf(mlike_result));
                        TipItem item1 = new TipItem(item.getNickname(), item.getContents(), item.getDate(), String.valueOf(mlike_result), item.getUnlike_result(), "true", "false");
                        TipItem item2 = new TipItem(item.getNickname(), item.getContents(), item.getDate(), String.valueOf(mlike_result), item.getUnlike_result());
                        firebaseDatabase.child("UserReview").child(sub_name).child(series).child(uid).child(item.getDate()).setValue(item1);
                        firebaseDatabase.child("Review").child(sub_name).child(series).child(item.getDate()).setValue(item2);
                        setButton(holder, item);
                    }
                });
                holder.unlike_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        munlike_result += 1;
                        holder.unlike_button.setChecked(true);
                        holder.unlike_result.setText(String.valueOf(munlike_result));
                        TipItem item1 = new TipItem(item.getNickname(), item.getContents(), item.getDate(), item.getLike_result(), String.valueOf(munlike_result), "false", "true");
                        TipItem item2 = new TipItem(item.getNickname(), item.getContents(), item.getDate(), item.getLike_result(), String.valueOf(munlike_result));
                        firebaseDatabase.child("UserReview").child(sub_name).child(series).child(uid).child(item.getDate()).setValue(item1);
                        firebaseDatabase.child("Review").child(sub_name).child(series).child(item.getDate()).setValue(item2);
                        setButton(holder, item);
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
        TextView unlike_result;
        TextView date;
        ToggleButton like_button;
        ToggleButton unlike_button;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            certificate = (TextView) itemView.findViewById(R.id.mytip_certificate_name);
            nickname = (TextView) itemView.findViewById(R.id.tip_item_nickname);
            contents = (TextView) itemView.findViewById(R.id.tip_item_contents);
            like_result = (TextView) itemView.findViewById(R.id.tip_like_result);
            unlike_result = (TextView) itemView.findViewById(R.id.tip_unlike_result);
            date = (TextView) itemView.findViewById(R.id.tip_item_date);
            like_button = (ToggleButton) itemView.findViewById(R.id.tip_like_button);
            unlike_button = (ToggleButton) itemView.findViewById(R.id.tip_unlike_button);
        }
    }

    private void setButton(MyViewHolder holder, TipItem item) {
        if (holder.like_button.isChecked()) {
            holder.like_button.setBackgroundDrawable(context.getDrawable(R.drawable.like_on_ic));
        } else {
            holder.like_button.setBackgroundDrawable(context.getDrawable(R.drawable.like_off_ic));
        }
        if (holder.unlike_button.isChecked()) {
            holder.unlike_button.setBackgroundDrawable(context.getDrawable(R.drawable.unlike_on_ic));
        } else {
            holder.unlike_button.setBackgroundDrawable(context.getDrawable(R.drawable.unlike_off_ic));
        }

        if (uid != null) {
            if (item.getIsLike().equals("true") || item.getIsUnLike().equals("true")) {
                holder.like_button.setEnabled(false);
                holder.unlike_button.setEnabled(false);
            }
        }

        if (holder.like_button.isChecked() || holder.unlike_button.isChecked()) {
            holder.like_button.setEnabled(false);
            holder.unlike_button.setEnabled(false);
        }
    }

}
