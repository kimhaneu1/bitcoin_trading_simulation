package org.example.backend.asset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.backend.asset.dto.response.TotalAssetResponseDto;
import org.example.backend.asset.service.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Asset", description = "자산 관련 API")
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @Operation(
            summary = "내 자산 현황 조회",
            description = "현재 로그인된 사용자의 총 자산, 보유 코인 자산, 보유 비중 등을 반환합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<TotalAssetResponseDto> getMyAssets() {
        TotalAssetResponseDto response = assetService.getMyAssets();
        return ResponseEntity.ok(response);
    }
}