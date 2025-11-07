package com.iclassq.video.service.impl;

import com.iclassq.video.dto.request.user.CreateUserDTO;
import com.iclassq.video.dto.request.user.UpdateUserDTO;
import com.iclassq.video.dto.response.user.UserResponseDTO;
import com.iclassq.video.entity.Role;
import com.iclassq.video.entity.User;
import com.iclassq.video.exception.DuplicateEntityException;
import com.iclassq.video.exception.ResourceNotFoundException;
import com.iclassq.video.mapper.UserMapper;
import com.iclassq.video.repository.RoleRepository;
import com.iclassq.video.repository.UserRepository;
import com.iclassq.video.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDTO create(CreateUserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateEntityException("Usuario", "username", dto.getUsername());
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEntityException("Usuario", "email", dto.getEmail());
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", dto.getRoleId()));

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto, role, hashedPassword);
        User savedUser = userRepository.save(user);

        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseDTOList(users);
    }

    @Override
    @Transactional
    public UserResponseDTO update(Integer id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new DuplicateEntityException("Usuario", "username", dto.getUsername());
            }
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateEntityException("Usuario", "email", dto.getEmail());
            }
        }

        userMapper.updateEntity(user, dto);

        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }

        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activate(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        user.setIsActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivate(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(Integer id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
