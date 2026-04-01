package com.iclass.video.controller;

import com.iclass.video.dto.request.auth.ResetPasswordDTO;
import com.iclass.video.dto.request.device.CreateDeviceDTO;
import com.iclass.video.dto.request.device.DeviceAssignAreaDTO;
import com.iclass.video.dto.request.device.UpdateDeviceDTO;
import com.iclass.video.dto.response.device.DeviceResponseDTO;
import com.iclass.video.dto.response.device.DeviceSyncResponseDTO;
import com.iclass.video.entity.DeviceArea;
import com.iclass.video.security.SecurityUtils;
import com.iclass.video.service.DeviceService;
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
    public ResponseEntity<List<DeviceResponseDTO>> findAll(
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) Integer areaId
    ) {
        List<DeviceResponseDTO> response;

        if (areaId != null) {
            response = deviceService.findByAreaId(areaId);
        } else if (branchId != null) {
            response = deviceService.findByBranchId(branchId);
        } else {
            response = deviceService.findAll();
        }

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
    public ResponseEntity<DeviceSyncResponseDTO> syncDevice(@PathVariable Integer id) {
        DeviceSyncResponseDTO response = deviceService.syncDevice(id);
        return ResponseEntity.ok(response);
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
