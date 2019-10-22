package com.example.q_moa.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q_moa.R;
import com.example.q_moa.favorites.Room.FavoriteViewModel;
import com.example.q_moa.favorites.Room.Favorite_Item;
import com.example.q_moa.lottie_fragment.LottieActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

public class InfoActivity extends AppCompatActivity {
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String certificate_name;
    private String uid;
    private String sub_name;
    private ImageView unfavorite_btn;
    private ImageView favorite_btn;
    private RecyclerView recyclerView;
    private TipRecyclerAdapter recyclerAdapter;
    private ArrayList<TipItem> items = new ArrayList<>();
    private TextView tip_null_text;
    private TextView tip_size_text;
    private String series;

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
            BooleanFavorite();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.info_tip_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new TipRecyclerAdapter(items, getApplicationContext(), sub_name, series);
        recyclerView.setAdapter(recyclerAdapter);

        unfavorite_btn = (ImageView) findViewById(R.id.info_unfavorite_btn);
        favorite_btn = (ImageView) findViewById(R.id.info_favorite_btn);
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

        // 즐겨찾기 버튼 설정 및 해제
        unfavorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid != null) {
                    favorite_btn.setVisibility(View.VISIBLE);
                    unfavorite_btn.setVisibility(View.GONE);

                    Favorite_Item word = new Favorite_Item(sub_name, series, "test1", "test2");
                    viewModel.insert(word);
                    setFavorite();

                } else {
                    Toast.makeText(InfoActivity.this, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorite_btn.setVisibility(View.GONE);
                unfavorite_btn.setVisibility(View.VISIBLE);

                Favorite_Item word = new Favorite_Item(sub_name, series, "test1", "test2");
                viewModel.delete(word);

                deletFavorite();

            }
        });

        setInfo("기관", agency_text);
        setInfo("계열", series_text);
        setInfo("분류", category_text);
        setTipList();
    }

    private void setFavorite() {
        Hashtable<String, Boolean> star = new Hashtable<>();
        star.put(uid, true);

        firebaseDatabase.child("국가기술자격").child("기술").child(sub_name).child(series).child("star").setValue(star);
    }

    private void deletFavorite() {
        final Query query = firebaseDatabase.child("국가기술자격").child("기술").child(sub_name).child(series).child("star").child(uid);
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

    private void BooleanFavorite() {
        firebaseDatabase.child("국가기술자격").child("기술").child(sub_name).child(series).child("star").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    boolean star = (boolean) dataSnapshot.getValue();

                    if (star == true) {
                        favorite_btn.setVisibility(View.VISIBLE);
                        unfavorite_btn.setVisibility(View.GONE);
                    } else {
                        favorite_btn.setVisibility(View.GONE);
                        unfavorite_btn.setVisibility(View.VISIBLE);
                    }
                } catch (NullPointerException e) {
                    favorite_btn.setVisibility(View.GONE);
                    unfavorite_btn.setVisibility(View.VISIBLE);

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        series = certificate_name.substring(1, 3);
        if (uid == null) {
            firebaseDatabase.child("Review").child(sub_name).child(series).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    TipItem item = dataSnapshot.getValue(TipItem.class);
                    TipItem data = new TipItem(item.getNickname(), item.getContents(), item.getDate(), item.getLike_result(), item.getUnlike_result());
                    items.add(data);
                    recyclerAdapter.notifyDataSetChanged();
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
        } else {
            firebaseDatabase.child("UserReview").child(sub_name).child(series).child(uid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    TipItem item = dataSnapshot.getValue(TipItem.class);
                    TipItem data = new TipItem(item.getNickname(), item.getContents(), item.getDate(), item.getLike_result(), item.getUnlike_result(), item.getIsLike(), item.getIsUnLike());
                    items.add(data);
                    recyclerAdapter.notifyDataSetChanged();
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
    }

    // Tip 작성되면 recyclerview 활성화
    private void addDataView() {
        if (items.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tip_null_text.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tip_null_text.setVisibility(View.VISIBLE);
            tip_size_text.setVisibility(View.GONE);
        }
    }
}
