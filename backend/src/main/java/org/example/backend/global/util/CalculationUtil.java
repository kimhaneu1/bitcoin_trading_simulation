package org.example.backend.global.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculationUtil {
    // 평가금액 계산 (보유 수량 × 현재가)
    public static BigDecimal calculateEvaluatedAmount(BigDecimal quantity, BigDecimal currentPrice) {
        return quantity.multiply(currentPrice);
    }

    // 주문 금액 계산 (가격 × 수량)
    public static BigDecimal calculateOrderAmount(BigDecimal price, BigDecimal quantity) {
        return price.multiply(quantity);
    }

    // 평균 매수가 계산((현재가 - 평균단가) / 평균단가 × 100)
    public static BigDecimal calculateAverageBuyPrice(BigDecimal prevTotal, BigDecimal newQuantity) {
        if (newQuantity.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return prevTotal.divide(newQuantity, 8, RoundingMode.HALF_UP);
    }

    // 수익률 계산 (ex: 15.23%)
    public static BigDecimal calculateProfitRate(BigDecimal avgBuyPrice, BigDecimal currentPrice) {
        if (avgBuyPrice.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return currentPrice.subtract(avgBuyPrice)
                .divide(avgBuyPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    // 보유 비중 계산 (ex: 전체 자산 대비 특정 코인의 비중)
    public static BigDecimal calculateHoldingRatio(BigDecimal amount, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return amount.divide(total, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
