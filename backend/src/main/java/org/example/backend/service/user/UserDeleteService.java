package org.example.backend.service.user;

import lombok.RequiredArgsConstructor;
import org.example.backend.entity.User;
import org.example.backend.exception.user.UserNotFoundException;
import org.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeleteService {

    private final UserRepository userRepository;

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일을 사용하는 유저를 찾을 수 없습니다." + email));
        userRepository.delete(user);
    }
}
