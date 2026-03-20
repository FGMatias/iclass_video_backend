package com.iclass.video.controller;

import com.iclass.video.dto.request.auth.ChangePasswordDTO;
import com.iclass.video.dto.request.auth.ResetPasswordDTO;
import com.iclass.video.dto.request.user.CreateBranchAdminDTO;
import com.iclass.video.dto.request.user.CreateCompanyAdminDTO;
import com.iclass.video.dto.request.user.CreateUserDTO;
import com.iclass.video.dto.request.user.UpdateUserDTO;
import com.iclass.video.dto.request.user.*;
import com.iclass.video.dto.response.user.UserResponseDTO;
import com.iclass.video.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<List<UserResponseDTO>> findAll(@RequestParam(required = false) Integer roleId) {
        List<UserResponseDTO> response;

        if (roleId != null) {
            response = userService.findByRoleId(roleId);
        } else {
            response = userService.findAll();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Integer id) {
        UserResponseDTO response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA')")
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid CreateUserDTO dto) {
        UserResponseDTO response = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/company-admin")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<UserResponseDTO> createCompanyAdmin(@RequestBody @Valid CreateCompanyAdminDTO dto) {
        UserResponseDTO response = userService.createCompanyAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/branch-admin")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA')")
    public ResponseEntity<UserResponseDTO> createBranchAdmin(@RequestBody @Valid CreateBranchAdminDTO dto) {
        UserResponseDTO response = userService.createBranchAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateUserDTO dto
    ) {
        UserResponseDTO response = userService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reassign-company")
    @PreAuthorize("hasRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<Void> reassignCompany(
            @PathVariable Integer id,
            @RequestBody @Valid ReassignCompanyDTO dto
    ) {
        userService.reassignCompany(id, dto.getCompanyId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reassign-branch")
    @PreAuthorize("hasRole('ADMINISTRADOR_EMPRESA')")
    public ResponseEntity<Void> reassignBranch(
            @PathVariable Integer id,
            @RequestBody @Valid ReassignBranchDTO dto
    ) {
        userService.reassignBranch(id, dto.getBranchId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<UserResponseDTO> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        userService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        userService.deactivate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> resetPassword(@PathVariable Integer id) {
        userService.resetPassword(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/change-password")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> changePassword(
            @PathVariable Integer id,
            @RequestBody @Valid ChangePasswordDTO dto
    ) {
        userService.changePassword(id, dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
