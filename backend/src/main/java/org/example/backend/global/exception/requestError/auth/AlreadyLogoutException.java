package org.example.backend.global.exception.requestError.auth;

import org.example.backend.global.exception.requestError.BusinessException;

public class AlreadyLogoutException extends BusinessException {
    public AlreadyLogoutException(String message) {super(message, null);}
}
