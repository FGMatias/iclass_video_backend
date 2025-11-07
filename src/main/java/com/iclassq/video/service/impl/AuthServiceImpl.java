package com.iclassq.video.service.impl;

import com.iclassq.video.dto.request.auth.LoginDTO;
import com.iclassq.video.dto.response.auth.AuthResponseDTO;
import com.iclassq.video.entity.User;
import com.iclassq.video.repository.UserRepository;
import com.iclassq.video.security.JwtService;
import com.iclassq.video.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO login(LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + dto.getUsername()));

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        authentication.getAuthorities()
                )
        );

        return AuthResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .roleId(user.getRole().getId())
                .roleName(user.getRole().getName())
                .build();
    }
}
