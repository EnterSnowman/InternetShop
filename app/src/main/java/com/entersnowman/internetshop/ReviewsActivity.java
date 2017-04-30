package com.entersnowman.internetshop;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.entersnowman.internetshop.adapter.ReviewAdapter;
import com.entersnowman.internetshop.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.listOfReviews) RecyclerView recyclerView;
    private DatabaseReference mDatabaseReviews;
    ArrayList<Review> reviews;
    ReviewAdapter reviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(R.string.reviews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviews,this);
        recyclerView.setAdapter(reviewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(ReviewsActivity.this,AddReviewActivity.class);
                intent.putExtra("category",getIntent().getStringExtra("category"));
                intent.putExtra("product_id",getIntent().getStringExtra("product_id"));
                startActivity(intent);
            }
        });
        mDatabaseReviews = FirebaseDatabase.getInstance().getReference()
                .child("products")
                .child(getIntent().getStringExtra("category"))
                .child(getIntent().getStringExtra("product_id"))
                .child("reviews");
        mDatabaseReviews.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    reviews.add(d.getValue(Review.class));
                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
