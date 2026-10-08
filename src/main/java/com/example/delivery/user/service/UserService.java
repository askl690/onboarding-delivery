package com.example.delivery.user.service;

import com.example.delivery.user.dto.request.SignupRequest;
import com.example.delivery.user.dto.response.UserResponse;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse signup(SignupRequest request) {
        // 1. 비밀번호 바이트 길이 검사(DB 조회 없이 할 수 있는 검사 먼저)
        if (request.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 72바이트 이하여야 합니다.");
        }

        // 2. 아이디 중복 검사
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다.");
        }

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 4. 저장(동시 가입으로 UNIQUE 위반 시 409)
        User user = new User(request.getLoginId(), encodedPassword, request.getRole());
        User saved;
        try {
            saved = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다.");
        }

        // 5. 응답 반환
        return new UserResponse(saved.getId(), saved.getLoginId(), saved.getRole(), saved.getCreatedAt(), saved.getUpdatedAt());
    }
}
