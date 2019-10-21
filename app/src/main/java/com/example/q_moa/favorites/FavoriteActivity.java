package com.example.q_moa.favorites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.q_moa.R;
import com.example.q_moa.favorites.Room.FavoriteListAdapter;
import com.example.q_moa.favorites.Room.FavoriteViewModel;
import com.example.q_moa.favorites.Room.Favorite_Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FavoriteListAdapter adapter;
    FavoriteViewModel viewModel;
    ImageButton btn_back;

    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (firebaseUser != null) {
            uid = firebaseUser.getUid();  // 사용자 uid 받아옴
        }

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
    }

    public void init() {

        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        recyclerView = findViewById(R.id.favorite_Recyclverview);
        adapter = new FavoriteListAdapter(this, viewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //observe : model의 LiveData를 관찰.
        viewModel.getAllFavorite().observe(this, new Observer<List<Favorite_Item>>() {
            @Override
            public void onChanged(@Nullable final List<Favorite_Item> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Favorite_Item myfavorite = adapter.getWordAtPosition(position);
                        // Delete the word
                        viewModel.delete(myfavorite);
                        deleteFirebase(myfavorite.getFavorite_name(), myfavorite.getSeries());
                    }
                });

        helper.attachToRecyclerView(recyclerView);

    }

    private void deleteFirebase(String name, String series) {
        final Query query = firebaseDatabase.child("국가기술자격").child("기술").child(name).child(series).child("star").child(uid);
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

}
