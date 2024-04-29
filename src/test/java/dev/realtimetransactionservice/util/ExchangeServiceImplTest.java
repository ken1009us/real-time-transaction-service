package dev.realtimetransactionservice.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeServiceImplTest {
    private ExchangeServiceImpl exchangeService;

    @BeforeEach
    public void setUp() {
        exchangeService = new ExchangeServiceImpl();
    }

    @Test
    public void convertCurrency_UsdToUsd() {
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal converted = exchangeService.convertCurrency("USD", amount);
        assertEquals(0, BigDecimal.valueOf(100).compareTo(converted), "USD to USD conversion should be unchanged.");
    }

    @Test
    public void convertCurrency_EurToUsd() {
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal expected = BigDecimal.valueOf(108.0);
        BigDecimal converted = exchangeService.convertCurrency("EUR", amount);
        assertEquals(0, expected.compareTo(converted), "100 EUR should be equal to 108 USD.");
    }

    @Test
    public void convertCurrency_JpyToUsd() {
        BigDecimal amount = BigDecimal.valueOf(10000);
        BigDecimal expected = BigDecimal.valueOf(75);
        BigDecimal converted = exchangeService.convertCurrency("JPY", amount);
        assertEquals(0, expected.compareTo(converted), "10000 JPY should be equal to 75 USD.");
    }

    @Test
    public void convertCurrency_UnknownCurrency() {
        BigDecimal amount = BigDecimal.valueOf(50);
        BigDecimal converted = exchangeService.convertCurrency("INR", amount);
        assertEquals(0, BigDecimal.valueOf(50).compareTo(converted), "Conversion with unknown currency should default to 1:1 rate.");
    }
}
