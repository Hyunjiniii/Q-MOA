package com.example.q_moa.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.q_moa.R;
import com.example.q_moa.login.LoginActivity;
import com.example.q_moa.lottie_fragment.LottieActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mFirebaseAuth.getCurrentUser();
    String stUid, stGuest, stPhoto,stname;
    private DatabaseReference myRef;
    ImageView main_nav_header_image;
    TextView main_nav_header_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.navbar_ic);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);

        main_nav_header_image = findViewById(R.id.main_nav_header_image);
        main_nav_header_name = findViewById(R.id.main_nav_header_name);


        CardView cardView = (CardView) findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            stUid = sharedPreferences.getString("Uid", "");
            stGuest = sharedPreferences.getString("login", "");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("users");

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        onClickNavIC();
        setHeaderView();

    }

    private void onClickNavIC() {
        navigationView = (NavigationView) findViewById(R.id.main_navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        drawerLayout.closeDrawers();
                        menuItem.setChecked(false);
                        break;
                    case R.id.favorites:
                        drawerLayout.closeDrawers();
                        menuItem.setChecked(false);
                        break;
                    case R.id.mytip:
                        drawerLayout.closeDrawers();
                        menuItem.setChecked(false);
                        break;
                    case R.id.help:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, LottieActivity.class));
                        finish();
                        menuItem.setChecked(false);
                        break;
                    case R.id.logout:
                        drawerLayout.closeDrawers();
                        menuItem.setChecked(false);
                        setLogout();

                        break;
                }
                return true;
            }
        });
    }

    //현재 사용자 확인
    @Override
    public void onStart() {
        super.onStart();
        if (user == null) {
            if (stGuest == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {

            }
        } else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();

            myRef.child("users").child(stUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                         stPhoto = dataSnapshot.child("photo").getValue().toString();
                         stname = dataSnapshot.child("name").getValue().toString();
                        Log.d("debug", "onDataChange: " + stname + stPhoto);

                        main_nav_header_name.setText(stname);
                        Glide.with(getApplicationContext()).load(stPhoto).into(main_nav_header_image);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public void setLogout(){
        if(stUid == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            stGuest = null;
        }else {
            mFirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

    }

    private void setHeaderView() {
        final View headerView = navigationView.getHeaderView(0);

        // header layout 상태바 높이만큼 내려줌
        int statusHeight = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            statusHeight = getResources().getDimensionPixelSize(resId);
            LinearLayout linearLayout = (LinearLayout) headerView.findViewById(R.id.main_nav_header_linear);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.topMargin = statusHeight;
            linearLayout.setLayoutParams(layoutParams);
        }

        // 프로필 이미지 라운딩
        ImageView profileImage = (ImageView) headerView.findViewById(R.id.main_nav_header_image);
        profileImage.setBackground(new ShapeDrawable(new OvalShape()));
        profileImage.setClipToOutline(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {  // 레이아웃 열려있으면 레이아웃 닫아줌
            drawerLayout.closeDrawers();
        } else
            super.onBackPressed();
    }
}
