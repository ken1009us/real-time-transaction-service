package dev.realtimetransactionservice.eventstore;

import dev.realtimetransactionservice.event.AuthorizeFundsEvent;
import dev.realtimetransactionservice.event.LoadFundsEvent;
import dev.realtimetransactionservice.event.TransactionEventHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentLinkedQueue;

public class InMemoryEventStore {
    private final ConcurrentLinkedQueue<Object> events = new ConcurrentLinkedQueue<>();
    private final TransactionEventHandler transactionEventHandler;

    @Autowired
    public InMemoryEventStore(TransactionEventHandler transactionEventHandler) {
        this.transactionEventHandler = transactionEventHandler;
    }

    public void addEvent(Object event) {
        events.add(event);
    }

    public void processEvent(Object event) {
        if (event instanceof LoadFundsEvent) {
            transactionEventHandler.handleLoadFundsEvent((LoadFundsEvent) event);
        } else if (event instanceof AuthorizeFundsEvent) {
            transactionEventHandler.handleAuthorizationEvent((AuthorizeFundsEvent) event);
        }

    }
}
