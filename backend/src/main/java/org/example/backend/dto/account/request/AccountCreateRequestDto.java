package org.example.backend.dto.account.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.backend.entity.enums.CurrencyType;

import java.math.BigDecimal;

public class AccountCreateRequestDto {
    @NotBlank
    private CurrencyType currency;
    @NotNull
    private BigDecimal balance;
    @NotNull
    private BigDecimal locked;
    @NotNull
    private BigDecimal avgBuyPrice;
    private boolean avgBuyPriceModified;
    @NotBlank
    private String unitCurrency;
}
