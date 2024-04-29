package dev.realtimetransactionservice.readmodel;

import dev.realtimetransactionservice.model.Amount;
import dev.realtimetransactionservice.model.DebitCredit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


public class UserBalanceReadModelTest {
    private UserBalanceReadModel userBalanceReadModel;

    @BeforeEach
    public void setUp() {
        userBalanceReadModel = new UserBalanceReadModel();
    }

    @Test
    public void testAddFunds_NewUser() {
        String userId = "user1";
        Amount initialAmount = new Amount("100", "USD", DebitCredit.CREDIT);
        userBalanceReadModel.addFunds(userId, initialAmount);

        Amount balance = userBalanceReadModel.getBalance(userId);
        Assertions.assertNotNull(balance);
        Assertions.assertEquals("100", balance.getAmount());
    }

    @Test
    public void testAddFunds_ExistingUser() {
        String userId = "user2";
        Amount initialAmount = new Amount("100", "USD", DebitCredit.CREDIT);
        userBalanceReadModel.addFunds(userId, initialAmount);

        Amount newAmount = new Amount("50", "USD", DebitCredit.CREDIT);
        userBalanceReadModel.addFunds(userId, newAmount);

        Amount balance = userBalanceReadModel.getBalance(userId);
        Assertions.assertNotNull(balance);
        Assertions.assertEquals("150", balance.getAmount());
    }

    @Test
    public void testSubtractFunds_ExistingUser() {
        String userId = "user3";
        userBalanceReadModel.addFunds(userId, new Amount("1200", "USD", DebitCredit.CREDIT));
        userBalanceReadModel.subtractFunds(userId, new Amount("200", "USD", DebitCredit.DEBIT));

        Amount balance = userBalanceReadModel.getBalance(userId);
        Assertions.assertNotNull(balance);
        Assertions.assertEquals("1000", new BigDecimal(balance.getAmount()).toString());
    }

    @Test
    public void testHasEnoughBalance_True() {
        String userId = "user4";
        userBalanceReadModel.addFunds(userId, new Amount("1500", "USD", DebitCredit.CREDIT));

        Assertions.assertTrue(userBalanceReadModel.hasEnoughBalance(userId, new Amount("1000", "USD", DebitCredit.DEBIT)));
    }

    @Test
    public void testHasEnoughBalance_False() {
        String userId = "user5";
        userBalanceReadModel.addFunds(userId, new Amount("200", "USD", DebitCredit.CREDIT));

        Assertions.assertFalse(userBalanceReadModel.hasEnoughBalance(userId, new Amount("500", "USD", DebitCredit.DEBIT)));
    }
}
