package dev.realtimetransactionservice.command;

import dev.realtimetransactionservice.event.LoadFundsEvent;
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
public class LoadFundsCommandHandlerTest {

    @InjectMocks
    private LoadFundsCommandHandler handler;

    @Mock
    private InMemoryEventStore eventStore;

    @Test
    public void testHandleLoadFundsCommand() {
        String userId = "user1";
        String messageId = "message1";
        Amount transactionAmount = new Amount("100", "USD", DebitCredit.CREDIT);

        LoadFundsCommand command = new LoadFundsCommand(userId, messageId, transactionAmount);
        handler.handle(command);

        verify(eventStore).addEvent(argThat(event -> {
            if (event instanceof LoadFundsEvent loadFundsEvent) {
                return loadFundsEvent.getUserId().equals(userId)
                        && loadFundsEvent.getMessageId().equals(messageId)
                        && loadFundsEvent.getTransactionAmount().equals(transactionAmount);
            }
            return false;
        }));
        verify(eventStore).processEvent(argThat(event -> {
            if (event instanceof LoadFundsEvent loadFundsEvent) {
                return loadFundsEvent.getUserId().equals(userId)
                        && loadFundsEvent.getMessageId().equals(messageId)
                        && loadFundsEvent.getTransactionAmount().equals(transactionAmount);
            }
            return false;
        }));
    }
}
