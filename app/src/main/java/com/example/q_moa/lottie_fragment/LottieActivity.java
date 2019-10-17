package com.example.q_moa.lottie_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.q_moa.R;
import com.example.q_moa.activity.MainActivity;
import com.example.q_moa.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LottieActivity extends AppCompatActivity {
    ViewPager lottie_viewpager;
    FloatingActionButton next_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);

        lottie_viewpager = findViewById(R.id.lottie_viewpager);
        next_fab = findViewById(R.id.next_fab);

        lottie_viewpager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        lottie_viewpager.setCurrentItem(0);

//        next_fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lottie_viewpager.setCurrentItem(getItem(+1));
//                switch (lottie_viewpager.getCurrentItem()){
//                    case 1:
//                        first_lottie.playAnimation();
//                        break;
//                    case 2:
//                        second_lottie.playAnimation();
//                        break;
//                    case 3:
//                        third_lottie.playAnimation();
//                        break;
//                    case 4:
//                        fourth_lottie.playAnimation();
//                        break;
//                    case 5:
//                        fifth_lottie.playAnimation();
//                        case6();
//                        break;
//                    case 6:
//                        last_lottie.playAnimation();
//                        break;
//                }
//            }
//        });

    }

    private int getItem(int i) {
        return lottie_viewpager.getCurrentItem() + i;
    }

    private class pagerAdapter extends FragmentPagerAdapter {

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                case 3:
                    return new FourthFragment();
                case 4:
                    return new FifthFragment();
                case 5:
                    case6();
                    next_fab.setVisibility(View.VISIBLE);
                    return new LastFragment();
                default:
                    return null;

            }

        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    private void case6() {
        next_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LottieActivity.this, MainActivity.class));
                finish();

            }
        });
    }

}
