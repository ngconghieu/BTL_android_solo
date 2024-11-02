package com.example.project.Object;

public class Cart {
    private int subPrice;
    private int quantity;
    private String foodName;


    public Cart() {
    }

    public int getSubPrice() {
        return subPrice;
    }

    public void setSubPrice(int subPrice) {
        this.subPrice = subPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Cart(int subPrice, int quantity, String foodName) {
        this.subPrice = subPrice;
        this.quantity = quantity;
        this.foodName = foodName;
    }
}
