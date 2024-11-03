package com.example.project.Object;

public class Order {
    private String orderId, username, payment, orderingMethod, orderTime, state;
    private int TotalPrice;
    private int phoneNumber;
    private Cart cart;

    public Order(String orderId, String username, String payment, String orderingMethod, String orderTime, String state, int totalPrice, int phoneNumber, Cart cart) {
        this.orderId = orderId;
        this.username = username;
        this.payment = payment;
        this.orderingMethod = orderingMethod;
        this.orderTime = orderTime;
        this.state = state;
        TotalPrice = totalPrice;
        this.phoneNumber = phoneNumber;
        this.cart = cart;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getOrderingMethod() {
        return orderingMethod;
    }

    public void setOrderingMethod(String orderingMethod) {
        this.orderingMethod = orderingMethod;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Order() {
    }
}
