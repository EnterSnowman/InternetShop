package com.entersnowman.internetshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.entersnowman.internetshop.R;
import com.entersnowman.internetshop.model.Review;

import java.util.ArrayList;

/**
 * Created by Valentin on 30.04.2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    ArrayList<Review> reviews;
    Context context;
    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        ReviewHolder reviewHolder = new ReviewHolder(v);
        return reviewHolder;
    }

    public ReviewAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.reviewText.setText(reviews.get(position).getReviewText());
        holder.buyerName.setText(reviews.get(position).getBuyerName());
        //holder.reviewDate.setText(reviews.get(position).getReviewDate());
        holder.reviewRating.setRating(reviews.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView reviewText;
        TextView buyerName;
        TextView reviewDate;
        RatingBar reviewRating;
        public ReviewHolder(View itemView) {
            super(itemView);
            reviewText = (TextView) itemView.findViewById(R.id.review_text);
            buyerName = (TextView) itemView.findViewById(R.id.buyer_name);
            reviewDate = (TextView) itemView.findViewById(R.id.review_date);
            reviewRating = (RatingBar) itemView.findViewById(R.id.review_rating);
        }
    }
}
