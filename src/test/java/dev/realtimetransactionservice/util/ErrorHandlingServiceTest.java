package dev.realtimetransactionservice.util;

import dev.realtimetransactionservice.model.Amount;
import dev.realtimetransactionservice.model.AuthorizationRequest;
import dev.realtimetransactionservice.model.DebitCredit;
import dev.realtimetransactionservice.model.LoadRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ErrorHandlingServiceTest {
    private final ErrorHandlingService service = new ErrorHandlingService();

    @Test
    public void validateMessageId_ValidId() {
        String messageId = UUID.randomUUID().toString();
        Assertions.assertDoesNotThrow(() -> service.validateMessageId(messageId));
    }

    @Test
    public void validateMessageId_NullId_ThrowsException() {
        Executable action = () -> service.validateMessageId(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, action);
        Assertions.assertEquals("Message ID must not be empty", thrown.getMessage());
    }

    @Test
    public void validateMessageId_EmptyId_ThrowsException() {
        Executable action = () -> service.validateMessageId("");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, action);
        Assertions.assertEquals("Message ID must not be empty", thrown.getMessage());
    }

    @Test
    public void validateUserId_ValidUserId() {
        Assertions.assertDoesNotThrow(() -> service.validateUserId("user123"));
    }

    @Test
    public void validateUserId_NullUserId_ThrowsException() {
        Executable action = () -> service.validateUserId(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, action);
        Assertions.assertEquals("User ID cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void validateUserId_EmptyUserId_ThrowsException() {
        Executable action = () -> service.validateUserId("");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, action);
        Assertions.assertEquals("User ID cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void validateTransactionAmountForLoadFunds_ValidAmount() {
        Amount amount = new Amount("100", "USD", DebitCredit.CREDIT);
        Assertions.assertDoesNotThrow(() -> service.validateTransactionAmountForLoadFunds(amount));
    }

    @Test
    public void validateTransactionAmountForLoadFunds_NullAmount_ThrowsException() {
        Executable action = () -> service.validateTransactionAmountForLoadFunds(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, action);
        Assertions.assertEquals("Transaction amount cannot be null", thrown.getMessage());
    }

    @Test
    public void validateTransactionAmountForLoadFunds_NegativeAmount_ThrowsException() {
        Amount amount = new Amount("-100", "USD", DebitCredit.CREDIT);
        Executable action = () -> service.validateTransactionAmountForLoadFunds(amount);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, action);
        Assertions.assertEquals("Transaction amount must be positive for load funds", thrown.getMessage());
    }

    @Test
    public void validateAuthorizationRequest_ValidRequest() {
        String messageId = UUID.randomUUID().toString();
        AuthorizationRequest request = new AuthorizationRequest("user1", new Amount("100", "USD", DebitCredit.DEBIT));
        Assertions.assertDoesNotThrow(() -> service.validateAuthorizationRequest(messageId, request));
    }

    @Test
    public void validateLoadRequest_ValidRequest() {
        String messageId = UUID.randomUUID().toString();
        LoadRequest request = new LoadRequest("user1", new Amount("200", "USD", DebitCredit.CREDIT));
        Assertions.assertDoesNotThrow(() -> service.validateLoadRequest(messageId, request));
    }
}
