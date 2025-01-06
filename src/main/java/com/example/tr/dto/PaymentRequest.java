package com.example.tr.dto;

public class PaymentRequest {
    private String refId;      // TR에서 사용
    private String tokenValue; // ISSUER에서 사용
    private double amount;     // 결제 금액

    public PaymentRequest() {}

    public PaymentRequest(String refId, String tokenValue, double amount) {
        this.refId = refId;
        this.tokenValue = tokenValue;
        this.amount = amount;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "refId='" + refId + '\'' +
                ", tokenValue='" + tokenValue + '\'' +
                ", amount=" + amount +
                '}';
    }
}
