package com.example.q_moa.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q_moa.adapter.InfoAdapter;
import com.example.q_moa.R;
import com.example.q_moa.favorites.Room.FavoriteViewModel;
import com.example.q_moa.favorites.Room.Favorite_Item;
import com.example.q_moa.floatingactionbutton.FloatingActionButton;
import com.example.q_moa.floatingactionbutton.FloatingScrollView;
import com.example.q_moa.location.LocationCheckActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String certificate_name;
    private String uid;
    private String sub_name;
    private RecyclerView TipRecyclerView;
    private TipRecyclerAdapter tipRecyclerAdapter;
    private ArrayList<TipItem> items = new ArrayList<>();
    private TextView tip_null_text;
    private TextView tip_size_text;
    private String series;
    private InfoAdapter infoAdapter;
    private ArrayList<Favorite_Item> favoriteItems = new ArrayList<>();
    private String time;

    FavoriteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();

        //favorite setting
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        if (intent.getExtras().getString("certificate") != null) {
            certificate_name = intent.getExtras().getString("certificate");  // 선택한 자격증 이름 받아옴
            sub_name = certificate_name.substring(5);  // 실기 또는 필기 자른 자격증이름
            series = certificate_name.substring(1, 3);
        }

        if (firebaseUser != null) {
            uid = firebaseUser.getUid();  // 사용자 uid 받아옴
//            BooleanFavorite();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        TipRecyclerView = (RecyclerView) findViewById(R.id.info_tip_recyclerview);
        TipRecyclerView.setLayoutManager(layoutManager);
        TipRecyclerView.setHasFixedSize(true);
        tipRecyclerAdapter = new TipRecyclerAdapter(items, getApplicationContext(), sub_name, series);
        TipRecyclerView.setAdapter(tipRecyclerAdapter);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        RecyclerView favoriteRecyclerview = (RecyclerView) findViewById(R.id.favorite_Recyclverview);
        favoriteRecyclerview.setLayoutManager(layoutManager1);
        favoriteRecyclerview.setHasFixedSize(true);
        infoAdapter = new InfoAdapter(getApplicationContext(), favoriteItems, viewModel, uid, sub_name, series);
        favoriteRecyclerview.setAdapter(infoAdapter);

        tip_null_text = (TextView) findViewById(R.id.info_tip_null_text);
        tip_size_text = (TextView) findViewById(R.id.info_tip_size_text);

        // Toolbar와 메인 Text에 자격증이름 설정
        TextView toolbar_text = (TextView) findViewById(R.id.info_toolbar_text);
        toolbar_text.setText(certificate_name);
        TextView main_text = (TextView) findViewById(R.id.info_main_text);
        main_text.setText(certificate_name);

        TextView agency_text = (TextView) findViewById(R.id.info_agency);
        TextView series_text = (TextView) findViewById(R.id.info_series);
        TextView category_text = (TextView) findViewById(R.id.info_category);

        // 뒤로가기 버튼 클릭 이벤트
        ImageButton back_button = (ImageButton) findViewById(R.id.info_back_btn);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Tip 작성 버튼 클릭 이벤트
        Button tip_button = (Button) findViewById(R.id.write_tip_btn);
        tip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid != null) {
                    Intent intent1 = new Intent(getApplicationContext(), TipActivity.class);
                    intent1.putExtra("FirebaseUid", uid);
                    intent1.putExtra("certificate", certificate_name);
                    startActivity(intent1);
                } else {
                    Toast.makeText(InfoActivity.this, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingScrollView scrollView = (FloatingScrollView) findViewById(R.id.info_scrollView);
        com.example.q_moa.floatingactionbutton.FloatingActionButton location_button = (FloatingActionButton) findViewById(R.id.location_floating);
        location_button.attachToScrollView(scrollView);
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LocationCheckActivity.class));
            }
        });

        setInfo("기관", agency_text);
        setInfo("계열", series_text);
        setInfo("분류", category_text);
        setTipList();
        setFavoriteDate();
    }

    // 기관, 계열, 분류 받아온 데이터 TextView에 적용
    private void setInfo(final String mchild, final TextView textView) {
        firebaseDatabase.child("국가기술자격").child("기술").child(sub_name).child(mchild).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView.setText(dataSnapshot.getValue().toString());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Tip RecyclerView에 넣어줌
    private void setTipList() {
        firebaseDatabase.child("UserReview").child(sub_name).child(series).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TipItem item = dataSnapshot.getValue(TipItem.class);
                TipItem data = new TipItem(item.getNickname(), item.getContents(), item.getDate(), item.getLike_result(), item.getUid());
                items.add(data);
                tipRecyclerAdapter.notifyDataSetChanged();
                tip_size_text.setVisibility(View.VISIBLE);
                tip_size_text.setText("(" + items.size() + ")");
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

    private void setFavoriteDate() {
        firebaseDatabase.child("날짜").child(series).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Favorite_Item item = dataSnapshot.getValue(Favorite_Item.class);
                Favorite_Item data = new Favorite_Item(item.getTime(), item.getText2(), item.getText1());
                time = item.getTime();
                favoriteItems.add(data);
                infoAdapter.notifyDataSetChanged();
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
            TipRecyclerView.setVisibility(View.VISIBLE);
            tip_null_text.setVisibility(View.GONE);
        } else {
            TipRecyclerView.setVisibility(View.GONE);
            tip_null_text.setVisibility(View.VISIBLE);
            tip_size_text.setVisibility(View.GONE);
        }
    }


}
