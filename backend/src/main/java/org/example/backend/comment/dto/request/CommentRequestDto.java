package org.example.backend.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "댓글 등록 요청 DTO")
public class CommentRequestDto {
    @Schema(description = "댓글 내용", example = "1등 축하합니다!")
    @NotBlank
    private String content;
}
