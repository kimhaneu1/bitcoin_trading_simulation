package org.example.backend.global.exception.requestError.comment;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.BusinessException;

public class CommentNotOwnerException extends BusinessException {
    public CommentNotOwnerException() {
        super(ErrorCode.COMMENT_NOT_OWNER);
    }
}
