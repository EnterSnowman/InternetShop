package com.entersnowman.internetshop.model;

import java.util.Map;

/**
 * Created by Valentin on 30.04.2017.
 */

public class Review {
    String reviewText;
    Long timestamp;
    String buyerName;
    float rating;

    public Review() {
    }

    public Review(String reviewText, Long reviewDate, String buyerName, float rating) {
        this.reviewText = reviewText;
        this.timestamp = reviewDate;
        this.buyerName = buyerName;
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Long  getReviewDate() {
        return timestamp;
    }

    public void setReviewDate(Long reviewDate) {
        this.timestamp = reviewDate;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
