package org.example.backend.global.exception.requestError.comment;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.BusinessException;

public class CommentMaxReachedException extends BusinessException {
    public CommentMaxReachedException() {
        super(ErrorCode.COMMENT_MAX_REACHED);
    }
}
