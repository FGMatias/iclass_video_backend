package com.iclass.video.service;

import com.iclass.video.dto.request.auth.LoginDTO;
import com.iclass.video.dto.request.user.*;
import com.iclass.video.dto.response.user.UserAuthResponseDTO;
import com.iclass.video.dto.response.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> findAll();
    List<UserResponseDTO> findByRoleId(Integer roleId);
    UserResponseDTO findById(Integer id);
    UserResponseDTO create(CreateUserDTO dto);
    UserResponseDTO createCompanyAdmin(CreateCompanyAdminDTO dto);
    UserResponseDTO createBranchAdmin(CreateBranchAdminDTO dto);
    UserResponseDTO update(Integer id, UpdateUserDTO dto);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
    void resetPassword(Integer id);
    void changePassword(Integer id, String newPassword);
    void reassignCompany(Integer userId, Integer newCompanyId);

    UserAuthResponseDTO login(LoginDTO dto);
}
