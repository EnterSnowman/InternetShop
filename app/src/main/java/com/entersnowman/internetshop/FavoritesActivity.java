package com.entersnowman.internetshop;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.entersnowman.internetshop.adapter.FavoritesAdapter;
import com.entersnowman.internetshop.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity {
    @BindView(R.id.favorites_list)
    RecyclerView favoritesList;
    FavoritesAdapter favoritesAdapter;
    ArrayList<Product> favoriteProducts;
    DatabaseReference mDatabaseFavorites;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabaseProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.your_favorites);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseProducts = FirebaseDatabase.getInstance().getReference().child("products");
        mDatabaseFavorites = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("favorite");
        favoriteProducts = new ArrayList<>();
        favoritesAdapter = new FavoritesAdapter(favoriteProducts,this);
        favoritesList.setAdapter(favoritesAdapter);
        favoritesList.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot d: dataSnapshot.getChildren()){
                    mDatabaseProducts.child(d.getKey().split("_")[0]).child(d.getKey().split("_")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot d2) {
                            Product p = d2.getValue(Product.class);
                            p.setCategory(d.getKey().split("_")[0]);
                            favoriteProducts.add(p);
                            favoritesAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
