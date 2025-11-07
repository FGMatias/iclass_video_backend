package com.iclassq.video.controller;

import com.iclassq.video.dto.request.user.CreateUserDTO;
import com.iclassq.video.dto.request.user.ResetPasswordDTO;
import com.iclassq.video.dto.request.user.UpdateUserDTO;
import com.iclassq.video.dto.response.user.UserResponseDTO;
import com.iclassq.video.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Integer id) {
        UserResponseDTO response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA')")
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid CreateUserDTO dto) {
        UserResponseDTO response = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateUserDTO dto
    ) {
        UserResponseDTO response = userService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<UserResponseDTO> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
//    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        userService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
//    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        userService.deactivate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reset-password")
//    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Integer id,
            @RequestBody @Valid ResetPasswordDTO dto
    ) {
        userService.resetPassword(id, dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
