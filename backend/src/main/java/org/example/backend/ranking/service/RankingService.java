package org.example.backend.ranking.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.asset.entity.Asset;
import org.example.backend.asset.repository.AssetRepository;
import org.example.backend.global.util.CalculationUtil;
import org.example.backend.global.util.RealTimePriceService;
import org.example.backend.ranking.dto.response.RankingResponseDto;
import org.example.backend.user.entity.User;
import org.example.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final RealTimePriceService priceService;

    public List<RankingResponseDto> getRanking() {
        List<User> users = userRepository.findAll();

        List<RankingResponseDto> unsorted = users.stream()
                .map(this::buildRankingInfo)
                .sorted(Comparator.comparing(RankingResponseDto::getTotalAssetAmount).reversed())
                .toList();

        AtomicInteger counter = new AtomicInteger(1);
        return unsorted.stream()
                .map(dto -> dto.toBuilder().rank(counter.getAndIncrement()).build())
                .toList();
    }

    private RankingResponseDto buildRankingInfo(User user) {
        List<Asset> assets = assetRepository.findByUser(user);

        BigDecimal totalEvaluated = BigDecimal.ZERO;
        Map<String, BigDecimal> evaluatedMap = new HashMap<>();

        for (Asset asset : assets) {
            BigDecimal currentPrice = priceService.getCurrentPrice(asset.getCoinSymbol());
            BigDecimal evaluatedAmount = CalculationUtil.calculateEvaluatedAmount(asset.getQuantity(), currentPrice);
            evaluatedMap.put(asset.getCoinSymbol(), evaluatedAmount);
            totalEvaluated = totalEvaluated.add(evaluatedAmount);
        }

        BigDecimal totalAsset = user.getKrwBalance().add(totalEvaluated);

        String topCoin = evaluatedMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");

        BigDecimal investedAmount = assets.stream()
                .map(a -> a.getAvgBuyPrice().multiply(a.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profitRate = CalculationUtil.calculateProfitRate(investedAmount, totalEvaluated);

        return RankingResponseDto.builder()
                .username(user.getUsername())
                .totalAssetAmount(totalAsset)
                .topCoin(topCoin)
                .profitRate(profitRate)
                .build();
    }
}
