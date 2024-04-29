package dev.realtimetransactionservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.realtimetransactionservice.model.Amount;
import dev.realtimetransactionservice.model.AuthorizationRequest;
import dev.realtimetransactionservice.model.DebitCredit;
import dev.realtimetransactionservice.model.LoadRequest;
import dev.realtimetransactionservice.readmodel.UserBalanceReadModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserBalanceReadModel userBalanceReadModel;

    @Test
    void testPing() throws Exception {
        mockMvc.perform(get("/transaction/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverTime").exists());
    }

    @Test
    public void testLoadFundsWithValidRequest() throws Exception {
        String userId = "user1";
        String messageId = UUID.randomUUID().toString();
        Amount transactionAmount = new Amount("100.00", "USD", DebitCredit.CREDIT);
        LoadRequest request = new LoadRequest(userId, transactionAmount);
        when(userBalanceReadModel.getBalance(userId)).thenReturn(transactionAmount);

        mockMvc.perform(put("/transaction/load/{messageId}", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAuthorizeFundsWithValidRequest() throws Exception {
        String userId = "user2";
        String messageId = UUID.randomUUID().toString();
        Amount transactionAmount = new Amount("100.00", "USD", DebitCredit.DEBIT);
        AuthorizationRequest request = new AuthorizationRequest(userId, transactionAmount);
        when(userBalanceReadModel.hasEnoughBalance(userId, transactionAmount)).thenReturn(true);

        mockMvc.perform(put("/transaction/authorization/{messageId}", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
