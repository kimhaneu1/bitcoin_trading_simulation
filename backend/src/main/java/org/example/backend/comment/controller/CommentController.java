package org.example.backend.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.comment.dto.request.CommentRequestDto;
import org.example.backend.comment.dto.response.CommentResponseDto;
import org.example.backend.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "랭킹 페이지 댓글 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 등록", description = "사용자가 댓글을 작성합니다. (최대 5개 제한)")
    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody @Valid CommentRequestDto dto) {
        commentService.addComment(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "댓글 목록 조회", description = "전체 댓글을 시간순으로 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @Operation(summary = "댓글 삭제", description = "자신이 작성한 댓글만 삭제할 수 있습니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
