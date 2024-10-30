package com.example.project;

public class Order {
    private String orderCode;
    private String name;
    private String phone;
    private String address;
    private String billDetails;
    private String date;
    private String totalBill;
    private String paymentMethod;

    public Order(String orderCode, String name, String phone, String address, String billDetails, String date, String totalBill, String paymentMethod) {
        this.orderCode = orderCode;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.billDetails = billDetails;
        this.date = date;
        this.totalBill = totalBill;
        this.paymentMethod = paymentMethod;
    }

    // Getters
    public String getOrderCode() { return orderCode; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getBillDetails() { return billDetails; }
    public String getDate() { return date; }
    public String getTotalBill() { return totalBill; }
    public String getPaymentMethod() { return paymentMethod; }
}
