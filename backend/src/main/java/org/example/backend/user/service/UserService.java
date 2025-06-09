package org.example.backend.user.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.user.entity.User;
import org.example.backend.global.enums.Role;
import org.example.backend.global.exception.requestError.user.DuplicateEmailException;
import org.example.backend.global.exception.requestError.user.DuplicatePhoneNumberException;
import org.example.backend.global.exception.requestError.user.UserNotFoundException;
import org.example.backend.user.repository.UserRepository;
import org.example.backend.user.dto.request.UserCreateRequestDto;
import org.example.backend.user.dto.request.UserUpdateRequestDto;
import org.example.backend.user.dto.response.UserResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto dto) {
        checkDuplicateEmail(dto.getEmail());
        checkDuplicatePhoneNumber(dto.getPhoneNumber());

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .role(Role.USER)
                .registeredAt(LocalDateTime.now())
                .krwBalance(new BigDecimal("10000000"))
                .build();

        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = findUserByEmail(email);
        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDto updateUser(String email, UserUpdateRequestDto dto) {
        User user  = findUserByEmail(email);

        String newPassword = passwordEncoder.encode(dto.getPassword());
        String newUsername = dto.getUsername();

        user.updateUser(newUsername, newPassword);

        return toDto(user);
    }

    private UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .registeredAt(user.getRegisteredAt())
                .build();
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일을 사용하는 사용자를 찾을 수 없습니다."));
    }

    private User findUserByPhoneNumber(String phoneNum) {
        return userRepository.findByPhoneNumber(phoneNum)
                .orElseThrow(() -> new UserNotFoundException("해당 닉네임을 사용하는 사용자를 찾을 수 없습니다."));
    }
    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    private void checkDuplicatePhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicatePhoneNumberException();
        }
    }


}