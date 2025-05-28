package org.example.backend.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.security.ValidPassword;

@Getter
@NoArgsConstructor
public class UserCreateRequestDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 4, max = 10, message = "최소 4글자, 최대 10글자 제한입니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @ValidPassword
    @Size(min = 8, max = 20, message = "최소 8글자, 최대 20글자 제한입니다.")
    private String password;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Size(min = 5, max = 30, message = "최소 5글자, 최대 30글자 제한입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
}
