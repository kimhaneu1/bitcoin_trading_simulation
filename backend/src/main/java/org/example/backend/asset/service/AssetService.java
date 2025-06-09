package org.example.backend.asset.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.asset.dto.response.CoinAssetResponseDto;
import org.example.backend.asset.dto.response.TotalAssetResponseDto;
import org.example.backend.asset.entity.Asset;
import org.example.backend.price.service.UpbitPriceService;
import org.example.backend.user.entity.User;
import org.example.backend.asset.repository.AssetRepository;
import org.example.backend.global.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final UpbitPriceService priceService;
    private final SecurityUtils securityUtils;

    public TotalAssetResponseDto getMyAssets() {
        User user = securityUtils.getCurrentUser();
        List<Asset> assets = assetRepository.findByUser(user);
        BigDecimal krwBalance = user.getKrwBalance();

        List<EvaluatedCoin> evaluatedCoins = assets.stream()
                .map(asset -> {
                    BigDecimal currentPrice = priceService.getCurrentPrice(asset.getCoinSymbol());
                    BigDecimal evaluatedAmount = currentPrice.multiply(asset.getQuantity());
                    BigDecimal profitRate = calculateProfitRate(asset.getAvgBuyPrice(), currentPrice);

                    return new EvaluatedCoin(asset.getCoinSymbol(), asset.getQuantity(), evaluatedAmount, profitRate);
                })
                .toList();

        // 총 코인 평가금액 계산
        BigDecimal totalCoinAsset = evaluatedCoins.stream()
                .map(EvaluatedCoin::evaluatedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 총 자산 = 현금 + 보유 코인 자산
        BigDecimal totalAsset = krwBalance.add(totalCoinAsset);

        // 코인별 DTO 구성
        List<CoinAssetResponseDto> coinDtos = evaluatedCoins.stream()
                .map(coin -> CoinAssetResponseDto.builder()
                        .coinSymbol(coin.coinSymbol())
                        .quantity(coin.quantity())
                        .evaluatedAmount(coin.evaluatedAmount())
                        .profitRate(coin.profitRate())
                        .holdingRatio(calculateHoldingRatio(coin.evaluatedAmount(), totalAsset)) // 전체 자산 대비 비중
                        .build())
                .toList();

        return TotalAssetResponseDto.builder()
                .totalAssetAmount(totalAsset)
                .krwBalance(krwBalance)
                .krwRatio(calculateHoldingRatio(krwBalance, totalAsset))
                .coinAssetAmount(totalCoinAsset)
                .coinRatio(calculateHoldingRatio(totalCoinAsset, totalAsset))
                .coinAssets(coinDtos)
                .build();
    }


    private BigDecimal calculateProfitRate(BigDecimal avgBuyPrice, BigDecimal currentPrice) {
        if (avgBuyPrice.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return currentPrice.subtract(avgBuyPrice)
                .divide(avgBuyPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private BigDecimal calculateHoldingRatio(BigDecimal amount, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return amount.divide(total, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private record EvaluatedCoin(
            String coinSymbol,
            BigDecimal quantity,
            BigDecimal evaluatedAmount,
            BigDecimal profitRate
    ) {}
}