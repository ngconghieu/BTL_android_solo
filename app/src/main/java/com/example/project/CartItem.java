package com.example.project;

public class CartItem {
    private String name;
    private int price;
    private int imageResource;
    private int quantity;

    public CartItem(String name, int price, int imageResource) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public int getTotalPrice() {
        return price * quantity; // Cách tính tổng giá
    }

    public int getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void increaseQuantity() {
    }

    public void decreaseQuantity() {
    }
}
