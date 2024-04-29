package dev.realtimetransactionservice.event;

import dev.realtimetransactionservice.model.Amount;
import dev.realtimetransactionservice.readmodel.UserBalanceReadModel;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionEventHandler {
    private final UserBalanceReadModel userBalanceReadModel;

    @Autowired
    public TransactionEventHandler(UserBalanceReadModel userBalanceReadModel) {
        this.userBalanceReadModel = userBalanceReadModel;
    }

    public void handleLoadFundsEvent(LoadFundsEvent event) {
        String userId = event.getUserId();
        Amount transactionAmount = event.getTransactionAmount();

        userBalanceReadModel.addFunds(userId, transactionAmount);
    }

    public void handleAuthorizationEvent(AuthorizeFundsEvent event) {
        String userId = event.getUserId();
        Amount transactionAmount = event.getTransactionAmount();
        boolean approved = event.isApproved();
        if (approved) {
            userBalanceReadModel.subtractFunds(userId, transactionAmount);
        }
    }
}
