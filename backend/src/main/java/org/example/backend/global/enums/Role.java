package org.example.backend.global.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 역할: USER(일반 사용자), ADMIN(관리자)")
public enum Role {
    USER,
    ADMIN
}
