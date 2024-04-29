package dev.realtimetransactionservice.command;

import dev.realtimetransactionservice.model.Amount;

public class AuthorizeFundsCommand {
    private final String userId;
    private final String messageId;
    private final Amount transactionAmount;
    private final boolean isApproved;

    public AuthorizeFundsCommand(String userId, String messageId, Amount transactionAmount, boolean isApproved) {
        this.userId = userId;
        this.messageId = messageId;
        this.transactionAmount = transactionAmount;
        this.isApproved = isApproved;
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

    public boolean isApproved() {
        return isApproved;
    }
}
