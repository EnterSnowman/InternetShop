package com.entersnowman.internetshop.model;

/**
 * Created by Valentin on 22.05.2017.
 */

public class Order{
    String id;
    String countOfProducts;
    String status;
    String dateOfMaking;
    String city;
    String warehouse;
    String kindOfPayment;

    public Order(String id, String countOfProducts, String status, String dateOfMaking, String city, String warehouse, String kindOfPayment) {
        this.id = id;
        this.countOfProducts = countOfProducts;
        this.status = status;
        this.dateOfMaking = dateOfMaking;
        this.city = city;
        this.warehouse = warehouse;
        this.kindOfPayment = kindOfPayment;
    }

    public Order() {

    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountOfProducts() {
        return countOfProducts;
    }

    public void setCountOfProducts(String countOfProducts) {
        this.countOfProducts = countOfProducts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateOfMaking() {
        return dateOfMaking;
    }

    public void setDateOfMaking(String dateOfMaking) {
        this.dateOfMaking = dateOfMaking;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getKindOfPayment() {
        return kindOfPayment;
    }

    public void setKindOfPayment(String kindOfPayment) {
        this.kindOfPayment = kindOfPayment;
    }
}
