package org.example.backend.exception.requestError.user;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.BusinessException;

public class UserListEmptyException extends BusinessException {

    public UserListEmptyException(String message) {
        super(message, ErrorCode.USER_LIST_EMPTY);
    }
}