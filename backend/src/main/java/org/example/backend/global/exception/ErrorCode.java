package org.example.backend.global.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "1000", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "1001", "서버 내부 오류입니다."),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "1100", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "1101", "해당 사용자를 찾을 수 없습니다."),
    USER_LIST_EMPTY(HttpStatus.NOT_FOUND, "1102", "등록된 사용자가 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "1103", "이미 등록된 이메일입니다."),
    DUPLICATE_PHONE_NUMBER(HttpStatus.CONFLICT, "1104", "이미 등록된 전화번호입니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "1200", "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "1201", "JWT 토큰이 만료되었습니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "1202", "JWT 토큰이 존재하지 않습니다."),
    TOKEN_TYPE_MISMATCH_EXCEPTION(HttpStatus.UNAUTHORIZED, "1203", "JWT 토큰 타입이 일치하지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "1204", "접근 권한이 없습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "1205", "리프레시 토큰을 찾을 수 없습니다."),
    TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "1206", "이미 사용된 리프레시 토큰입니다."),
    ALREADY_LOGOUT(HttpStatus.BAD_REQUEST, "1207", "이미 로그아웃된 사용자입니다."),

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "1300", "주문 내역을 찾을 수 없습니다."),
    ORDER_NOT_UPDATABLE(HttpStatus.BAD_REQUEST, "1301", "체결된 주문은 수정할 수 없습니다."),
    ORDER_NOT_DELETABLE(HttpStatus.BAD_REQUEST, "1302", "체결된 주문은 삭제할 수 없습니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "1303", "보유 현금이 부족합니다."),
    INSUFFICIENT_ASSET(HttpStatus.BAD_REQUEST, "1304", "보유한 코인 수량이 부족합니다."),

    ASSET_NOT_FOUND(HttpStatus.NOT_FOUND, "1400", "해당 자산을 보유하고 있지 않습니다."),
    ASSET_LIST_EMPTY(HttpStatus.NOT_FOUND, "1401", "보유한 자산이 없습니다."),

    COMMENT_MAX_REACHED(HttpStatus.BAD_REQUEST, "1500", "댓글은 최대 10개까지 작성할 수 있습니다."),
    COMMENT_NOT_OWNER(HttpStatus.FORBIDDEN, "1501", "본인의 댓글만 삭제할 수 있습니다."),

    I_DONT_KNOW(HttpStatus.BAD_REQUEST, "2000", "왜 에러임?"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
