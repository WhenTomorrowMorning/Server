package com.y2sg.wtm.domain.user.application;

import java.util.Optional;

import com.y2sg.wtm.global.DefaultAssert;
import com.y2sg.wtm.global.config.security.token.UserPrincipal;
import com.y2sg.wtm.domain.user.domain.User;
import com.y2sg.wtm.global.payload.ApiResponse;
import com.y2sg.wtm.domain.user.domain.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<?> readByUser(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(user);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(user.get()).build();
        return ResponseEntity.ok(apiResponse);
    }
}
