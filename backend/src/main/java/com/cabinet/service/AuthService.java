package com.cabinet.service;

import com.cabinet.config.JwtTokenProvider;
import com.cabinet.dto.LoginRequest;
import com.cabinet.dto.LoginResponse;
import com.cabinet.model.User;
import com.cabinet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        if (!user.isEnabled()) {
            throw new BadCredentialsException("Compte désactivé");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponse(token, user.getEmail(), user.getNom(), user.getRole().name());
    }
}
