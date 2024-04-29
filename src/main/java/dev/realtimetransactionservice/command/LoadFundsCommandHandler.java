package dev.realtimetransactionservice.command;

import dev.realtimetransactionservice.event.LoadFundsEvent;
import dev.realtimetransactionservice.eventstore.InMemoryEventStore;
import org.springframework.beans.factory.annotation.Autowired;

public class LoadFundsCommandHandler {
    private final InMemoryEventStore eventStore;

    @Autowired
    public LoadFundsCommandHandler(InMemoryEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void handle(LoadFundsCommand command) {
        LoadFundsEvent event = new LoadFundsEvent(
                command.getUserId(),
                command.getMessageId(),
                command.getTransactionAmount()
        );
        eventStore.addEvent(event);
        eventStore.processEvent(event);
    }
}
