package com.entersnowman.internetshop.model;

/**
 * Created by Valentin on 24.04.2017.
 */

public class Product {
    String name;
    float price;
    String photo_url;
    boolean isAvailable;
    float rating;
    String description;
    String category;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    public  Product(){

    }
    public Product(String name, float price, String photo_url, boolean isAvailable,String description) {
        this.name = name;
        this.price = price;
        this.photo_url = photo_url;
        this.isAvailable = isAvailable;
        this.description = description;
    }

    public Product(String name, float price, String photo_url, boolean isAvailable, float rating) {
        this.name = name;
        this.price = price;
        this.photo_url = photo_url;
        this.isAvailable = isAvailable;
        this.rating = rating;
    }

    public float getRating() {

        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
