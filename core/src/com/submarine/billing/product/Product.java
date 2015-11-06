package com.submarine.billing.product;

/**
 * Created by sargis on 10/30/14.
 */
public class Product {
    public String currency;
    public float price;
    public String id;

    public Product(String id, float price, String currency) {
        this.id = id;
        this.price = price;
        this.currency = currency;
    }

    public Product() {
    }
}
