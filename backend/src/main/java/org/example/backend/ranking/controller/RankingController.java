package org.example.backend.ranking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.backend.ranking.dto.response.RankingResponseDto;
import org.example.backend.ranking.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "사용자 자산 랭킹 API")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "자산 랭킹 조회", description = "모든 사용자의 자산 랭킹을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "랭킹 조회 성공")
    @GetMapping
    public ResponseEntity<List<RankingResponseDto>> getRanking() {
        return ResponseEntity.ok(rankingService.getRanking());
    }
}
