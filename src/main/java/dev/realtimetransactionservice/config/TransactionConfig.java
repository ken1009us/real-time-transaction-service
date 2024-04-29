package dev.realtimetransactionservice.config;

import dev.realtimetransactionservice.command.AuthorizeFundsCommandHandler;
import dev.realtimetransactionservice.command.LoadFundsCommandHandler;
import dev.realtimetransactionservice.event.TransactionEventHandler;
import dev.realtimetransactionservice.eventstore.InMemoryEventStore;
import dev.realtimetransactionservice.readmodel.UserBalanceReadModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionConfig {

    @Bean
    public UserBalanceReadModel userBalanceReadModel() {
        return new UserBalanceReadModel();
    }

    @Bean
    public InMemoryEventStore inMemoryEventStore(TransactionEventHandler transactionEventHandler) {
        return new InMemoryEventStore(transactionEventHandler);
    }

    @Bean
    public AuthorizeFundsCommandHandler authorizeFundsCommandHandler(InMemoryEventStore eventStore) {
        return new AuthorizeFundsCommandHandler(eventStore);
    }

    @Bean
    public LoadFundsCommandHandler loadFundsCommandHandler(InMemoryEventStore eventStore) {
        return new LoadFundsCommandHandler(eventStore);
    }

    @Bean
    public TransactionEventHandler transactionEventHandler(UserBalanceReadModel userBalanceReadModel) {
        return new TransactionEventHandler(userBalanceReadModel);
    }
}
