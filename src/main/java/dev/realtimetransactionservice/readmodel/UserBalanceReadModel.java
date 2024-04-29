package dev.realtimetransactionservice.readmodel;

import dev.realtimetransactionservice.model.Amount;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class UserBalanceReadModel {
    private final ConcurrentHashMap<String, Amount> userBalances = new ConcurrentHashMap<>();

    public void addFunds(String userId, Amount transactionAmount) {
        userBalances.compute(userId, (key, existingBalance) -> {
            if (existingBalance == null) {
                return transactionAmount;
            } else {
                String newAmount = new BigDecimal(existingBalance.getAmount()).add(new BigDecimal(transactionAmount.getAmount())).toString();
                return new Amount(newAmount, transactionAmount.getCurrency(), transactionAmount.getDebitOrCredit());
            }
        });
    }

    public void subtractFunds(String userId, Amount transactionAmount) {
        userBalances.compute(userId, (key, existingBalance) -> {
            if (existingBalance == null) {
                return transactionAmount;
            } else {
                String newAmount = new BigDecimal(existingBalance.getAmount()).subtract(new BigDecimal(transactionAmount.getAmount())).toString();
                return new Amount(newAmount, transactionAmount.getCurrency(), transactionAmount.getDebitOrCredit());
            }
        });
    }

    public boolean hasEnoughBalance(String userId, Amount transactionAmount) {
        Amount balance = userBalances.get(userId);
        if (balance == null) {
            return false;
        }
        return new BigDecimal(balance.getAmount()).compareTo(new BigDecimal(transactionAmount.getAmount())) >= 0;
    }

    public Amount getBalance(String userId) {
        return userBalances.get(userId);
    }
}
