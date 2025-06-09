package org.example.backend.ranking.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.ranking.dto.response.RankingResponseDto;
import org.example.backend.asset.entity.Asset;
import org.example.backend.price.service.UpbitPriceService;
import org.example.backend.user.entity.User;
import org.example.backend.asset.repository.AssetRepository;
import org.example.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final UpbitPriceService priceService;

    public List<RankingResponseDto> getRanking() {
        List<User> users = userRepository.findAll();

        List<RankingResponseDto> unsorted = users.stream()
                .map(this::buildRankingInfo)
                .toList();

        AtomicInteger counter = new AtomicInteger(1);

        return unsorted.stream()
                .sorted(Comparator.comparing(RankingResponseDto::getTotalAssetAmount).reversed())
                .map(dto -> dto.toBuilder().rank(counter.getAndIncrement()).build())
                .toList();
    }

    private RankingResponseDto buildRankingInfo(User user) {
        List<Asset> assets = assetRepository.findByUser(user);

        BigDecimal totalAssetAmount = user.getKrwBalance();
        Map<String, BigDecimal> evaluatedMap = new HashMap<>();
        BigDecimal totalEvaluated = BigDecimal.ZERO;

        for (Asset asset : assets) {
            BigDecimal currentPrice = priceService.getCurrentPrice(asset.getCoinSymbol());
            BigDecimal evaluatedAmount = currentPrice.multiply(asset.getQuantity());
            evaluatedMap.put(asset.getCoinSymbol(), evaluatedAmount);
            totalEvaluated = totalEvaluated.add(evaluatedAmount);
        }

        totalAssetAmount = totalAssetAmount.add(totalEvaluated);

        String topCoin = evaluatedMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");

        BigDecimal profitRate = calculateProfitRate(user, assets, totalEvaluated);

        return RankingResponseDto.builder()
                .username(user.getUsername())
                .totalAssetAmount(totalAssetAmount)
                .topCoin(topCoin)
                .profitRate(profitRate)
                .build();
    }

    private BigDecimal calculateProfitRate(User user, List<Asset> assets, BigDecimal totalEvaluated) {
        BigDecimal invested = assets.stream()
                .map(a -> a.getAvgBuyPrice().multiply(a.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (invested.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalEvaluated.subtract(invested)
                .divide(invested, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
