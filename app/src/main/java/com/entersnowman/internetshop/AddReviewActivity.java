package com.entersnowman.internetshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.entersnowman.internetshop.model.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReviewActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReviews;
    private FirebaseAuth mAuth;
    @BindView(R.id.send_review)
    Button send_button;
    @BindView(R.id.review_rating)
    RatingBar reviewRating;
    @BindView(R.id.review_edit_text)
    EditText review_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        ButterKnife.bind(this);
        mAuth=  FirebaseAuth.getInstance();
        mDatabaseReviews = FirebaseDatabase.getInstance().getReference()
                .child("products")
                .child(getIntent().getStringExtra("category"))
                .child(getIntent().getStringExtra("product_id"))
                .child("reviews");
        getSupportActionBar().setTitle(R.string.give_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewRating.getRating()!=0) {
                    if (!review_edit.getText().toString().equals("")) {
                        Review review = new Review();
                        review.setRating(reviewRating.getRating());
                        review.setBuyerName(mAuth.getCurrentUser().getDisplayName());
                        review.setReviewText(review_edit.getText().toString());
                        mDatabaseReviews.child(mAuth.getCurrentUser().getUid()).setValue(review, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(AddReviewActivity.this,"Your review added",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else {
                        Toast.makeText(AddReviewActivity.this,R.string.input_review,Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AddReviewActivity.this,R.string.rate_product,Toast.LENGTH_SHORT).show();
                }
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
