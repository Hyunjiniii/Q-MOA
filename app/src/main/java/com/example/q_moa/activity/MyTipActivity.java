package com.example.q_moa.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
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

public class MyTipActivity extends AppCompatActivity {
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private RecyclerView recyclerView;
    private TipRecyclerAdapter recyclerAdapter;
    private ArrayList<TipItem> items = new ArrayList<>();
    private TextView tip_null_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tip);

        tip_null_text = (TextView) findViewById(R.id.mytip_nodata_text);
        ImageButton back_button = (ImageButton) findViewById(R.id.mytip_back_btn);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.mytip_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new TipRecyclerAdapter(items, getApplicationContext());
        recyclerView.setAdapter(recyclerAdapter);

        if (firebaseUser != null)
            uid = firebaseUser.getUid();

        firebaseDatabase.child("MyReview").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TipItem item = dataSnapshot.getValue(TipItem.class);
                TipItem data = new TipItem(item.getNickname(), item.getContents(), item.getDate(), item.getCertificate());
                items.add(data);
                recyclerAdapter.notifyDataSetChanged();
                Log.d("Called", "Activity");
                addDataView();
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

    // Tip 작성되면 recyclerview 활성화
    private void addDataView() {
        if (items.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tip_null_text.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tip_null_text.setVisibility(View.VISIBLE);
        }
    }
}
