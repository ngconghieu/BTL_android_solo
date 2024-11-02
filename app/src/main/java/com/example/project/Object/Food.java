package com.example.project.Object;

import java.util.List;

public class Food {
    private String foodName, details;
    private int price;
    private int discount;

    public Food(String foodName, List<String> imageFood) {
        this.foodName = foodName;
        this.imageFood = imageFood;
    }

    public Food(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<String> getImageFood() {
        return imageFood;
    }

    public Food(String foodName, String details, int price, int discount) {
        this.foodName = foodName;
        this.details = details;
        this.price = price;
        this.discount = discount;
    }

    public void setImageFood(List<String> imageFood) {
        this.imageFood = imageFood;
    }

    public Food(List<String> imageFood) {
        this.imageFood = imageFood;
    }

    public Food() {
    }

    public Food(String foodName, String details, int price, int discount, List<String> imageFood) {
        this.foodName = foodName;
        this.details = details;
        this.price = price;
        this.discount = discount;
        this.imageFood = imageFood;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodName='" + foodName + '\'' +
                ", details='" + details + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", imageFood=" + imageFood +
                '}';
    }

    private List<String> imageFood;

}
