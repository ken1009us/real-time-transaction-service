package dev.realtimetransactionservice.model;

public class AuthorizationRequest {
    private String userId;
    private Amount transactionAmount;

    public AuthorizationRequest(String userId, Amount transactionAmount) {
        this.userId = userId;
        this.transactionAmount = transactionAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Amount getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Amount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public String toString() {
        return "AuthorizationRequest{" +
                "userId='" + userId + '\'' +
                ", transactionAmount=" + transactionAmount +
                '}';
    }
}
