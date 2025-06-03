package org.example.backend.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;
import org.example.backend.security.ValidPassword;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank
    @Size(max = 10)
    private String username;

    @NotBlank
    @Size(max = 20)
    @ValidPassword
    private String password;

    @NotBlank
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    private String phoneNumber;
}
