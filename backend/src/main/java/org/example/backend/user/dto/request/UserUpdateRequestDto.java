package org.example.backend.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;
import org.example.backend.global.security.validator.ValidPassword;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Schema(description = "수정할 사용자 이름", example = "김길동")
    @NotBlank
    @Size(max = 10)
    private String username;

    @Schema(description = "새 비밀번호", example = "NewPassword456!")
    @NotBlank
    @Size(max = 20)
    @ValidPassword
    private String password;

    @Schema(description = "휴대전화 번호", example = "010-5678-1234")
    @NotBlank
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    private String phoneNumber;
}

