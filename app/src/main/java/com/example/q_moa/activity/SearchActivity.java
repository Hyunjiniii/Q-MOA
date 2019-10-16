package com.example.q_moa.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.q_moa.R;
import com.example.q_moa.search_fragment.SearchHistoryFragment;
import com.example.q_moa.search_fragment.SearchRecyclerFragment;


public class SearchActivity extends AppCompatActivity {
    private SearchHistoryFragment HistoryFragment;
    private SearchRecyclerFragment RecyclerFragment;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        HistoryFragment = (SearchHistoryFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        RecyclerFragment = new SearchRecyclerFragment();

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // SearchView에 검색어 입력 시 목록 Fragment로
                if (!newText.isEmpty()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, RecyclerFragment).commit();
                }
                return false;
            }
        });

        ImageButton back_btn = (ImageButton) findViewById(R.id.search_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public SearchView FindsearchView() {
        return searchView;
    }
}

// TipActivity로 이동 시 오류잡기
