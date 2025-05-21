package org.example.backend.user;

import lombok.RequiredArgsConstructor;
import org.example.backend.auth.Role;
import org.example.backend.common.exception.user.DuplicateEmailException;
import org.example.backend.common.exception.user.DuplicateUsernameException;
import org.example.backend.common.exception.user.UserNotFoundException;
import org.example.backend.user.dto.request.UserCreateRequestDto;
import org.example.backend.user.dto.request.UserUpdateRequestDto;
import org.example.backend.user.dto.response.UserResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto createUser(UserCreateRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException();
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateUsernameException();
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .email(dto.getEmail())
                .balance(BigDecimal.ZERO)
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .balance(user.getBalance())
                .build();
    }

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일을 사용하는 유저를 찾을 수 없습니다." + email));
        userRepository.delete(user);
    }

    public UserResponseDto getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일을 사용하는 유저를 찾을 수 없습니다." + email));
        return toDto(user);
    }

    public UserResponseDto getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("해당 닉네임을 사용하는 유저를 찾을 수 없습니다." + username));
        return toDto(user);
    }

    public UserResponseDto updateUser(String email, UserUpdateRequestDto dto) {
        User user  = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일을 사용하는 유저를 찾을 수 없습니다." + email));

        String newPassword = dto.getPassword() != null ?
                passwordEncoder.encode(dto.getPassword()) : user.getPassword();

        String newUsername = dto.getUsername() != null ?
                dto.getUsername() : user.getUsername();

        user.updateUser(newUsername, newPassword);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .balance(user.getBalance())
                .build();
    }

    private UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .balance(user.getBalance())
                .build();
    }
}
