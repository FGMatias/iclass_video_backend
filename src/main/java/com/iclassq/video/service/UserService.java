package com.iclassq.video.service;

import com.iclassq.video.dto.request.user.CreateUserDTO;
import com.iclassq.video.dto.request.user.UpdateUserDTO;
import com.iclassq.video.dto.response.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO create(CreateUserDTO dto);
    UserResponseDTO findById(Integer id);
    List<UserResponseDTO> findAll();
    UserResponseDTO update(Integer id, UpdateUserDTO dto);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
    void resetPassword(Integer id, String newPassword);
}
