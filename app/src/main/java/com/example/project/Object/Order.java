package com.example.project.Object;

import java.util.List;

public class Order {
    private String orderId, payment, orderingMethod, orderTime, state;
    private int TotalPrice;
    private List<Cart> cart;
    Address address;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Order(String orderId, String payment, String orderingMethod, String orderTime,
                 String state, int totalPrice, List<Cart> cart, Address address) {
        this.orderId = orderId;
        this.payment = payment;
        this.orderingMethod = orderingMethod;
        this.orderTime = orderTime;
        this.state = state;
        TotalPrice = totalPrice;
        this.cart = cart;
        this.address = address;
    }

    public Order() {
    }
}
