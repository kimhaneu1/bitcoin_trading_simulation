package org.example.backend.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.global.security.ValidPassword;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequestDto {

    @Schema(description = "사용자 이름", example = "홍길동")
    @NotBlank
    @Size(max = 10)
    private String username;

    @Schema(description = "비밀번호", example = "SecurePass123!")
    @NotBlank
    @ValidPassword
    @Size(max = 20)
    private String password;

    @Schema(description = "이메일 주소", example = "hong@example.com")
    @NotBlank
    @Size(max = 25)
    @Email
    private String email;

    @Schema(description = "휴대전화 번호", example = "010-1234-5678")
    @NotBlank
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    private String phoneNumber;
}
