package dev.realtimetransactionservice.command;

import dev.realtimetransactionservice.event.AuthorizeFundsEvent;
import dev.realtimetransactionservice.eventstore.InMemoryEventStore;
import dev.realtimetransactionservice.model.Amount;
import dev.realtimetransactionservice.model.DebitCredit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorizeFundsCommandHandlerTest {
    @InjectMocks
    private AuthorizeFundsCommandHandler handler;

    @Mock
    private InMemoryEventStore eventStore;

    @Test
    public void testHandleAuthorizeFundsCommand() {
        String userId = "user1";
        String messageId = "message1";
        Amount transactionAmount = new Amount("100", "USD", DebitCredit.CREDIT);
        boolean isApproved = true;

        AuthorizeFundsCommand command = new AuthorizeFundsCommand(userId, messageId, transactionAmount, isApproved);
        handler.handle(command);

        verify(eventStore).addEvent(argThat(event -> {
            if (event instanceof AuthorizeFundsEvent authorizeFundsEvent) {
                return authorizeFundsEvent.getUserId().equals(userId)
                        && authorizeFundsEvent.getMessageId().equals(messageId)
                        && authorizeFundsEvent.getTransactionAmount().equals(transactionAmount)
                        && authorizeFundsEvent.isApproved() == isApproved;
            }
            return false;
        }));
        verify(eventStore).processEvent(argThat(event -> {
            if (event instanceof AuthorizeFundsEvent authorizeFundsEvent) {
                return authorizeFundsEvent.getUserId().equals(userId)
                        && authorizeFundsEvent.getMessageId().equals(messageId)
                        && authorizeFundsEvent.getTransactionAmount().equals(transactionAmount)
                        && authorizeFundsEvent.isApproved() == isApproved;
            }
            return false;
        }));
    }
}
