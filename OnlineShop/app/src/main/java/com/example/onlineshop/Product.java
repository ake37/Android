package com.example.onlineshop;

import androidx.annotation.NonNull;

public class Product {
    private float price;
    private float weight;
    private String name;
    private String description;

    public Product(String name, String description, float price, float weight)
    {
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
    }

    public float getPrice() {
        return price;
    }

    public float getWeight() {
        return weight;
    }

    public String getDescription() {
        return description + "\n" + "Price: " + getPrice() + " RON\n" + "Weight: " + getWeight() + " g";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
