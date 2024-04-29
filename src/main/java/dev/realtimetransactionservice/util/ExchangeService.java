package dev.realtimetransactionservice.util;

import java.math.BigDecimal;

public interface ExchangeService {
    BigDecimal convertCurrency(String fromCurrency, BigDecimal amount);
}
