package com.example.q_moa.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.q_moa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TipMoreActivity extends AppCompatActivity {
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView recyclerView;
    private TipRecyclerAdapter recyclerAdapter;
    private ArrayList<TipItem> items = new ArrayList<>();
    private String certificate_name;
    private String sub_name;
    private String series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_more);

        final Intent intent = getIntent();
        certificate_name = intent.getExtras().getString("certificate");  // 선택한 자격증 이름 받아옴
        sub_name = certificate_name.substring(5);  // 실기 또는 필기 자른 자격증이름
        String tip_size = intent.getExtras().getString("tip_size");
        series = certificate_name.substring(1, 3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.tip_more_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new TipRecyclerAdapter(items, getApplicationContext(), sub_name, series);
        recyclerView.setAdapter(recyclerAdapter);

        ImageButton back_button = (ImageButton) findViewById(R.id.tip_more_back_btn);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView tip_size_textview = (TextView) findViewById(R.id.tip_more_size_text);
        tip_size_textview.setText("(" + tip_size + ")");

        setTipList();
    }

    // Tip RecyclerView에 넣어줌
    private void setTipList() {
        firebaseDatabase.child("Review").child(sub_name).child(series).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TipItem item = dataSnapshot.getValue(TipItem.class);
                TipItem data = new TipItem(item.getNickname(), item.getContents(), item.getResult(), item.getDate(), item.isLike());
                items.add(data);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
