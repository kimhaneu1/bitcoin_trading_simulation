package org.example.backend.exception.requestError.auth;

import org.example.backend.exception.requestError.BusinessException;

public class AlreadyLogoutException extends BusinessException {
    public AlreadyLogoutException(String message) {super(message, null);}
}
