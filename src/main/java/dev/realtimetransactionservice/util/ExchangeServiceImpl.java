package dev.realtimetransactionservice.util;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Service to convert currency.
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {
    private Map<String, BigDecimal> exchangeRates;

    public ExchangeServiceImpl() {
        exchangeRates = new HashMap<>();
        exchangeRates.put("USD", BigDecimal.ONE);
        exchangeRates.put("EUR", new BigDecimal("1.08"));
        exchangeRates.put("JPY", new BigDecimal("0.0075"));
    }

    /**
     * Convert the currency.
     *
     * @param fromCurrency the currency to convert from
     * @param amount the amount to convert
     * @return the converted amount
     */
    @Override
    public BigDecimal convertCurrency(String fromCurrency, BigDecimal amount) {
        BigDecimal rate = exchangeRates.getOrDefault(fromCurrency, BigDecimal.ONE);
        return amount.multiply(rate);
    }
}
