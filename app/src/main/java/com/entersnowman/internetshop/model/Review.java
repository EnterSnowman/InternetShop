package com.entersnowman.internetshop.model;

/**
 * Created by Valentin on 30.04.2017.
 */

public class Review {
    String reviewText;
    String reviewDate;
    String buyerName;
    float rating;

    public Review() {
    }

    public Review(String reviewText, String reviewDate, String buyerName, float rating) {
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.buyerName = buyerName;
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
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
