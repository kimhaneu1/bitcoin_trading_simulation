package org.example.backend.global.exception.requestError.user;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.BusinessException;

public class UserListEmptyException extends BusinessException {

    public UserListEmptyException(String message) {
        super(message, ErrorCode.USER_LIST_EMPTY);
    }
}