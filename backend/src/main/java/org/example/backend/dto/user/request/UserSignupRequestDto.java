package org.example.backend.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {
    private String username;
    private String password;
    private String email;
}
