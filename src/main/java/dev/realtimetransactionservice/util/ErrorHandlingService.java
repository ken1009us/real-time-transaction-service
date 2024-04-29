package dev.realtimetransactionservice.util;

import dev.realtimetransactionservice.model.Amount;
import dev.realtimetransactionservice.model.AuthorizationRequest;
import dev.realtimetransactionservice.model.DebitCredit;
import dev.realtimetransactionservice.model.LoadRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ErrorHandlingService {

    public void validateAuthorizationRequest(String messageId, AuthorizationRequest request) {
        validateUserId(request.getUserId());
        validateMessageId(messageId);
        validateTransactionAmountForAuthorizeFunds(request.getTransactionAmount());
        validateDebitCreditForAuthorize(request.getTransactionAmount());
    }

    public void validateLoadRequest(String messageId, LoadRequest request) {
        validateUserId(request.getUserId());
        validateMessageId(messageId);
        validateTransactionAmountForLoadFunds(request.getTransactionAmount());
        validateDebitCreditForLoad(request.getTransactionAmount());
    }

    public void validateMessageId(String messageId) {
        if (messageId == null || messageId.trim().isEmpty()) {
            throw new IllegalArgumentException("Message ID must not be empty");
        }
        if (messageId.length() != 36) {
            throw new IllegalArgumentException("Invalid message ID format: " + messageId + " - UUID must be exactly 36 characters long.");
        }
        try {
            UUID uuid = UUID.fromString(messageId);
            if (uuid.variant() != UUID.randomUUID().variant() || uuid.version() <= 0) {
                throw new IllegalArgumentException("The UUID is not valid as it does not represent a 128-bit value.");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid message ID format: " + messageId + " - The UUID to be validated should be 128 bit long.", e);
        }
    }

    public void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
    }

    public void validateTransactionAmountForLoadFunds(Amount transactionAmount) {
        if (transactionAmount == null) {
            throw new IllegalArgumentException("Transaction amount cannot be null");
        }
        if (new BigDecimal(transactionAmount.getAmount()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive for load funds");
        }
    }

    public void validateTransactionAmountForAuthorizeFunds(Amount transactionAmount) {
        if (transactionAmount == null) {
            throw new IllegalArgumentException("Transaction amount cannot be null");
        }
        if (new BigDecimal(transactionAmount.getAmount()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive for authorize funds");
        }
    }

    private void validateDebitCreditForLoad(Amount transactionAmount) {
        if (transactionAmount.getDebitOrCredit() != DebitCredit.CREDIT) {
            throw new IllegalArgumentException("Transaction amount must be a credit for load funds");
        }
    }

    private void validateDebitCreditForAuthorize(Amount transactionAmount) {
        if (transactionAmount.getDebitOrCredit() != DebitCredit.DEBIT) {
            throw new IllegalArgumentException("Transaction amount must be a debit for Authorize funds");
        }
    }
}