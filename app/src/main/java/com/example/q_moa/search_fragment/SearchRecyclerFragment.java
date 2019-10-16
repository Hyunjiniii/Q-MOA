package com.example.q_moa.search_fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.q_moa.R;
import com.example.q_moa.activity.SearchActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerFragment extends Fragment {
    public static List<String> data;
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private SearchRecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_recycler_fragment, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.search_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        putData();

        SearchView searchView = ((SearchActivity)getActivity()).FindsearchView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                List<String> newList = new ArrayList<>();

                for (String name : SearchRecyclerFragment.data) {
                    if (name.toLowerCase().contains(userInput))
                        newList.add(name);
                }

                recyclerAdapter.updateList(newList);

                return true;
            }
        });

        return rootView;
    }

    private void putData() {
        data = new ArrayList<>();

        firebaseDatabase.child("국가기술자격").child("기술").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    data.add("[실기] " + snapshot.getKey());
                    data.add("[필기] " + snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerAdapter = new SearchRecyclerAdapter(getContext(), data);
        recyclerView.setAdapter(recyclerAdapter);
    }
}
