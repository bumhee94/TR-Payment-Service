package com.example.tr.dto;

public class PaymentRequest {
    private String refId;
    private double amount;

    // Constructors, Getters, Setters
    public PaymentRequest() {}

    public PaymentRequest(double amount, String refId) {
        this.amount = amount;
        this.refId = refId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
