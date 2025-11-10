package com.iclassq.video.controller;

import com.iclassq.video.dto.request.auth.ResetPasswordDTO;
import com.iclassq.video.dto.request.device.CreateDeviceDTO;
import com.iclassq.video.dto.request.device.DeviceAssignAreaDTO;
import com.iclassq.video.dto.request.device.UpdateDeviceDTO;
import com.iclassq.video.dto.response.device.DeviceResponseDTO;
import com.iclassq.video.entity.DeviceArea;
import com.iclassq.video.security.SecurityUtils;
import com.iclassq.video.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final SecurityUtils securityUtils;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<List<DeviceResponseDTO>> findAll() {
        List<DeviceResponseDTO> response = deviceService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<DeviceResponseDTO> findById(@PathVariable Integer id) {
        DeviceResponseDTO response = deviceService.getDeviceWithCurrentArea(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<DeviceResponseDTO> create(@RequestBody @Valid CreateDeviceDTO dto) {
        Integer adminUserId = securityUtils.getCurrentUserId();
        DeviceResponseDTO response = deviceService.create(dto, adminUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<DeviceResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateDeviceDTO dto
    ) {
        DeviceResponseDTO response = deviceService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<DeviceResponseDTO> delete(@PathVariable Integer id) {
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reassign")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> reassign(
            @PathVariable Integer id,
            @RequestBody @Valid DeviceAssignAreaDTO dto
    ) {
        Integer adminUserId = securityUtils.getCurrentUserId();
        deviceService.reassign(id, dto, adminUserId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        deviceService.deactivate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/sync")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateSync(@PathVariable Integer id) {
        deviceService.updateLastSync(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<List<DeviceArea>> getHistory(@PathVariable Integer id) {
        List<DeviceArea> response = deviceService.getHistory(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Integer id,
            @RequestBody @Valid ResetPasswordDTO dto
    ) {
        deviceService.resetPassword(id, dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
