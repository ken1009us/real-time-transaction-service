package dev.realtimetransactionservice.eventstore;

import dev.realtimetransactionservice.event.LoadFundsEvent;
import dev.realtimetransactionservice.event.TransactionEventHandler;
import dev.realtimetransactionservice.model.Amount;
import dev.realtimetransactionservice.model.DebitCredit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class InMemoryEventStoreIntegrationTest {
    @InjectMocks
    private InMemoryEventStore eventStore;

    @Mock
    private TransactionEventHandler transactionEventHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventStore = new InMemoryEventStore(transactionEventHandler);
    }

    @Test
    public void testAddAndProcessEvent_LoadFundsEvent() {
        LoadFundsEvent loadEvent = new LoadFundsEvent("user1", "message1", new Amount("100", "USD", DebitCredit.CREDIT));

        eventStore.addEvent(loadEvent);
        eventStore.processEvent(loadEvent);

        verify(transactionEventHandler).handleLoadFundsEvent(loadEvent);
    }

    @Test
    public void testAddAndProcessEvent_AuthorizeFundsEvent() {
        LoadFundsEvent authorizeEvent = new LoadFundsEvent("user2", "message2", new Amount("200", "USD", DebitCredit.CREDIT));

        eventStore.addEvent(authorizeEvent);
        eventStore.processEvent(authorizeEvent);

        verify(transactionEventHandler).handleLoadFundsEvent(authorizeEvent);
    }
}
