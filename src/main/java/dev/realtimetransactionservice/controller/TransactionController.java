package dev.realtimetransactionservice.controller;

import dev.realtimetransactionservice.command.AuthorizeFundsCommand;
import dev.realtimetransactionservice.command.AuthorizeFundsCommandHandler;
import dev.realtimetransactionservice.command.LoadFundsCommand;
import dev.realtimetransactionservice.command.LoadFundsCommandHandler;
import dev.realtimetransactionservice.model.*;
import dev.realtimetransactionservice.readmodel.UserBalanceReadModel;
import dev.realtimetransactionservice.util.ErrorHandlingService;
import dev.realtimetransactionservice.util.ExchangeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final LoadFundsCommandHandler loadFundsCommandHandler;
    private final AuthorizeFundsCommandHandler authorizeFundsCommandHandler;
    private final UserBalanceReadModel userBalanceReadModel;
    private final ErrorHandlingService errorHandlingService;
    private final ExchangeServiceImpl exchangeService;

    @Autowired
    public TransactionController(LoadFundsCommandHandler loadFundsCommandHandler,
                                 AuthorizeFundsCommandHandler authorizeFundsCommandHandler,
                                 UserBalanceReadModel userBalanceReadModel,
                                 ErrorHandlingService errorHandlingService,
                                 ExchangeServiceImpl exchangeService) {
        this.loadFundsCommandHandler = loadFundsCommandHandler;
        this.authorizeFundsCommandHandler = authorizeFundsCommandHandler;
        this.userBalanceReadModel = userBalanceReadModel;
        this.errorHandlingService = errorHandlingService;
        this.exchangeService = exchangeService;
    }

    /*
    * This endpoint is used to check if the server is up and running.
     */
    @GetMapping("/ping")
    public ResponseEntity<Ping> ping() {
        Ping pingResponse = new Ping();
        pingResponse.setServerTime(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return ResponseEntity.ok(pingResponse);
    }

    /*
    * This endpoint is used to load funds into the user's account.
     */
    @PutMapping("/load/{messageId}")
    public ResponseEntity<LoadResponse> loadFunds(@PathVariable String messageId, @RequestBody LoadRequest request){
        errorHandlingService.validateLoadRequest(messageId, request);
        String userId = request.getUserId();
        Amount transactionAmount = request.getTransactionAmount();
        BigDecimal convertedAmount = exchangeService.convertCurrency(transactionAmount.getCurrency(), new BigDecimal(transactionAmount.getAmount()));
        Amount newAmount = new Amount(convertedAmount.toString(), "USD", transactionAmount.getDebitOrCredit());
        LoadFundsCommand command = new LoadFundsCommand(userId, messageId, newAmount);
        loadFundsCommandHandler.handle(command);

        Amount balance = userBalanceReadModel.getBalance(userId);
        LoadResponse response = new LoadResponse(userId, messageId, balance);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
    * This endpoint is used to authorize funds from the user's account.
     */
    @PutMapping("/authorization/{messageId}")
    public ResponseEntity<AuthorizationResponse> authorizeFunds(@PathVariable String messageId, @RequestBody AuthorizationRequest request){
        errorHandlingService.validateAuthorizationRequest(messageId, request);
        String userId = request.getUserId();
        Amount transactionAmount = request.getTransactionAmount();
        BigDecimal convertedAmount = exchangeService.convertCurrency(transactionAmount.getCurrency(), new BigDecimal(transactionAmount.getAmount()));
        Amount newAmount = new Amount(convertedAmount.toString(), "USD", transactionAmount.getDebitOrCredit());

        boolean hasEnoughBalance = userBalanceReadModel.hasEnoughBalance(userId, newAmount);

        AuthorizeFundsCommand command = new AuthorizeFundsCommand(userId, messageId, newAmount, hasEnoughBalance);
        authorizeFundsCommandHandler.handle(command);

        Amount balance = userBalanceReadModel.getBalance(userId);
        ResponseCode responseCode = hasEnoughBalance ? ResponseCode.APPROVED : ResponseCode.DECLINED;
        AuthorizationResponse response = new AuthorizationResponse(userId, messageId, responseCode, balance);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
