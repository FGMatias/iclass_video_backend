package com.iclassq.video.mapper;

import com.iclassq.video.dto.request.user.CreateUserDTO;
import com.iclassq.video.dto.request.user.UpdateUserDTO;
import com.iclassq.video.dto.response.user.UserResponseDTO;
import com.iclassq.video.dto.response.user.UserSimpleDTO;
import com.iclassq.video.entity.Role;
import com.iclassq.video.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(CreateUserDTO dto, Role role) {
        return User.builder()
                .role(role)
                .username(dto.getUsername())
                .password(dto.getPassword())
                .name(dto.getName())
                .paternalSurname(dto.getPaternalSurname())
                .maternalSurname(dto.getMaternalSurname())
                .documentNumber(dto.getDocumentNumber())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .isActive(true)
                .build();
    }

    public void updateEntity(User user, UpdateUserDTO dto) {
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPaternalSurname() != null) user.setPaternalSurname(dto.getPaternalSurname());
        if (dto.getMaternalSurname() != null) user.setMaternalSurname(dto.getMaternalSurname());
        if (dto.getDocumentNumber() != null) user.setDocumentNumber(dto.getDocumentNumber());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getIsActive() != null) user.setIsActive(dto.getIsActive());
    }

    public UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .roleId(user.getRole().getId())
                .roleName(user.getRole().getName())
                .username(user.getUsername())
                .name(user.getName())
                .paternalSurname(user.getPaternalSurname())
                .maternalSurname(user.getMaternalSurname())
                .documentNumber(user.getDocumentNumber())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UserSimpleDTO toSimpleDTO(User user) {
        return UserSimpleDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public List<UserResponseDTO> toResponseDTOList(List<User> users) {
        return users.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UserSimpleDTO> toSimpleDTOList(List<User> users) {
        return users.stream()
                .map(this::toSimpleDTO)
                .collect(Collectors.toList());
    }
}
