package com.iclassq.video.controller;

import com.iclassq.video.dto.request.auth.LoginDTO;
import com.iclassq.video.dto.request.device.DeviceAssignAreaDTO;
import com.iclassq.video.dto.request.device.RegisterDeviceDTO;
import com.iclassq.video.dto.response.device.DeviceAuthResponseDTO;
import com.iclassq.video.dto.response.device.DeviceRegisterResponseDTO;
import com.iclassq.video.dto.response.device.DeviceResponseDTO;
import com.iclassq.video.security.SecurityUtils;
import com.iclassq.video.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceAuthController {

    private final DeviceService deviceService;
    private final SecurityUtils securityUtils;

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<DeviceRegisterResponseDTO> register(@RequestBody @Valid RegisterDeviceDTO dto) {
        Integer adminUserId = securityUtils.getCurrentUserId();
        DeviceRegisterResponseDTO response = deviceService.register(dto, adminUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<DeviceAuthResponseDTO> login(@RequestBody @Valid LoginDTO dto) {
        DeviceAuthResponseDTO response = deviceService.login(
                dto.getUsername(),
                dto.getPassword()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{deviceId}/reassign")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> reassign(
            @PathVariable Integer deviceId,
            @RequestBody @Valid DeviceAssignAreaDTO dto
    ) {
        Integer adminUserId = securityUtils.getCurrentUserId();
        deviceService.reassign(deviceId, dto, adminUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{deviceId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<DeviceResponseDTO> getDevice(@PathVariable Integer deviceId) {
        DeviceResponseDTO response = deviceService.getDeviceWithCurrentArea(deviceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{deviceId}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> deactivate(@PathVariable Integer deviceId) {
        deviceService.deactivate(deviceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deviceId}/sync")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateSync(@PathVariable Integer deviceId) {
        deviceService.updateLastSync(deviceId);
        return ResponseEntity.ok().build();
    }
}
