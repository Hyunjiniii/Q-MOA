package com.example.q_moa.favorites;

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

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FavoriteListAdapter adapter;
    FavoriteViewModel viewModel;
    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

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
                    }
                });

        helper.attachToRecyclerView(recyclerView);

    }

}
