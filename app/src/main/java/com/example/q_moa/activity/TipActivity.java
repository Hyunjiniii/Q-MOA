package com.example.q_moa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.q_moa.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TipActivity extends AppCompatActivity {
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private String uid;
    private String sub_name;
    private String certificate_name;
    private EditText tip_input;
    private String nickname;
    private String contents;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfDate = new SimpleDateFormat("YYYY-MM-dd (HH:mm:ss)");
    String formatDate = sdfDate.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        Intent intent = getIntent();
        uid = intent.getExtras().getString("FirebaseUid");
        certificate_name = intent.getExtras().getString("certificate");
        sub_name = certificate_name.substring(5);
        tip_input = (EditText) findViewById(R.id.tip_input);

        ImageButton back_button = (ImageButton) findViewById(R.id.tip_back_btn);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 사용자 이름 받아옴
        firebaseDatabase.child("users").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nickname = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // 완료버튼 클릭 시 Firebase에 데이터 업데이트
        FloatingActionButton ok_button = (FloatingActionButton) findViewById(R.id.tip_floating_button);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tip_input.length() != 0) {
                    contents = tip_input.getText().toString();
                    TipItem tipItem = new TipItem(nickname, contents, formatDate, "0", "0", "false", "false");
                    TipItem tipItem1 = new TipItem(nickname, contents, formatDate, "0", "0");
                    TipItem tipItem2 = new TipItem(nickname, contents, formatDate, certificate_name);
                    FirebaseDatabase.getInstance().getReference().child("MyReview").child(uid).child(formatDate).setValue(tipItem2);
                    FirebaseDatabase.getInstance().getReference().child("UserReview").child(sub_name).child(certificate_name.substring(1, 3)).child(uid).child(formatDate).setValue(tipItem);
                    FirebaseDatabase.getInstance().getReference().child("Review").child(sub_name).child(certificate_name.substring(1, 3)).child(formatDate).setValue(tipItem1);
                    finish();
                } else {
                    Toast.makeText(TipActivity.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
