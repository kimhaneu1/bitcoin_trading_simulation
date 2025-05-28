package org.example.backend.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Size(min = 4, max = 10)
    private String username;

    @Size(min = 6, max = 20)
    private String password;
}
