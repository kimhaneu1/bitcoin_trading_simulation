package org.example.backend.account.dto.response;

import org.example.backend.common.enums.CurrencyType;

import java.math.BigDecimal;

public class AccountResponseDto {
    private Long id;
    private CurrencyType currency;
    private BigDecimal balance;
    private BigDecimal locked;
    private BigDecimal avgBuyPrice;
    private boolean avgBuyPriceModified;
    private String unitCurrency;
}
