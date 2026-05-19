package com.cabinet.service;

import com.cabinet.dto.UserDTO;
import com.cabinet.model.Role;
import com.cabinet.model.User;
import com.cabinet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserDTO dto, String password) {
        User user = User.builder()
                .nom(dto.getNom())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(password))
                .role(Role.valueOf(dto.getRole()))
                .enabled(dto.isEnabled())
                .build();
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setNom(dto.getNom());
        user.setEmail(dto.getEmail());
        user.setRole(Role.valueOf(dto.getRole()));
        user.setEnabled(dto.isEnabled());
        return userRepository.save(user);
    }

    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
