package dev.realtimetransactionservice.event;

import dev.realtimetransactionservice.model.Amount;
import dev.realtimetransactionservice.model.DebitCredit;
import dev.realtimetransactionservice.readmodel.UserBalanceReadModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionEventHandlerTest {
    @InjectMocks
    private TransactionEventHandler handler;

    @Mock
    private UserBalanceReadModel userBalanceReadModel;

    @Test
    public void testHandleLoadFundsEvent() {
        String userId = "user1";
        String messageId = "message1";
        Amount transactionAmount = new Amount("100", "USD", DebitCredit.CREDIT);
        LoadFundsEvent event = new LoadFundsEvent(userId, messageId, transactionAmount);
        handler.handleLoadFundsEvent(event);

        verify(userBalanceReadModel).addFunds(userId, transactionAmount);
    }

    @Test
    public void testHandleAuthorizationEvent_Approved() {
        String userId = "user1";
        String messageId = "message1";
        Amount transactionAmount = new Amount("100", "USD", DebitCredit.CREDIT);
        boolean isApproved = true;
        AuthorizeFundsEvent event = new AuthorizeFundsEvent(userId, messageId, transactionAmount, isApproved);
        handler.handleAuthorizationEvent(event);

        verify(userBalanceReadModel).subtractFunds(userId, transactionAmount);
    }

    @Test
    public void testHandleAuthorizationEvent_NotApproved() {
        String userId = "user1";
        String messageId = "message1";
        Amount transactionAmount = new Amount("100", "USD", DebitCredit.CREDIT);
        boolean isApproved = false;
        AuthorizeFundsEvent event = new AuthorizeFundsEvent(userId, messageId, transactionAmount, isApproved);
        handler.handleAuthorizationEvent(event);

        verify(userBalanceReadModel, never()).subtractFunds(userId, transactionAmount);
    }
}
