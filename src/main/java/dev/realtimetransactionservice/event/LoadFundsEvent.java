package dev.realtimetransactionservice.event;

import dev.realtimetransactionservice.model.Amount;

public class LoadFundsEvent {
    private final String userId;
    private final String messageId;
    private final Amount transactionAmount;

    public LoadFundsEvent(String userId, String messageId, Amount transactionAmount) {
        this.userId = userId;
        this.messageId = messageId;
        this.transactionAmount = transactionAmount;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public Amount getTransactionAmount() {
        return transactionAmount;
    }
}
