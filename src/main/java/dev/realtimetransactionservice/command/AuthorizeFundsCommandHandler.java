package dev.realtimetransactionservice.command;

import dev.realtimetransactionservice.event.AuthorizeFundsEvent;
import dev.realtimetransactionservice.eventstore.InMemoryEventStore;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthorizeFundsCommandHandler {
    private final InMemoryEventStore eventStore;

    @Autowired
    public AuthorizeFundsCommandHandler(InMemoryEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void handle(AuthorizeFundsCommand command) {
        AuthorizeFundsEvent event = new AuthorizeFundsEvent(
                command.getUserId(),
                command.getMessageId(),
                command.getTransactionAmount(),
                command.isApproved()
        );
        eventStore.addEvent(event);
        eventStore.processEvent(event);
    }
}
