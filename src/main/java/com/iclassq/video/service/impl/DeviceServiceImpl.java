package com.iclassq.video.service.impl;

import com.iclassq.video.dto.request.device.DeviceAssignAreaDTO;
import com.iclassq.video.dto.request.device.RegisterDeviceDTO;
import com.iclassq.video.dto.response.device.DeviceAuthResponseDTO;
import com.iclassq.video.dto.response.device.DeviceRegisterResponseDTO;
import com.iclassq.video.dto.response.device.DeviceResponseDTO;
import com.iclassq.video.entity.*;
import com.iclassq.video.exception.DeviceNotAssignedException;
import com.iclassq.video.exception.DuplicateEntityException;
import com.iclassq.video.exception.ResourceNotFoundException;
import com.iclassq.video.mapper.DeviceMapper;
import com.iclassq.video.repository.*;
import com.iclassq.video.security.JwtService;
import com.iclassq.video.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceAreaRepository deviceAreaRepository;
    private final AreaRepository areaRepository;
    private final AreaVideoRepository areaVideoRepository;
    private final UserRepository userRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DeviceMapper deviceMapper;

    @Override
    @Transactional
    public DeviceRegisterResponseDTO register(RegisterDeviceDTO dto, Integer adminUserId) {
        Area area = areaRepository.findById(dto.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área", dto.getAreaId()));

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", adminUserId));

        DeviceType deviceType = deviceTypeRepository.findById(dto.getDeviceTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de dispositivo", dto.getDeviceTypeId()));

        if (deviceRepository.existsByDeviceUsername(dto.getDeviceUsername())) {
            throw new DuplicateEntityException("Dispositivo", "username", dto.getDeviceUsername());
        }

        String hashedPassword = passwordEncoder.encode(dto.getDevicePassword());

        Device device = deviceMapper.toEntity(dto, deviceType, admin, hashedPassword);
        Device savedDevice = deviceRepository.save(device);

        DeviceArea assignment = DeviceArea.builder()
                .device(savedDevice)
                .area(area)
                .assignedBy(admin)
                .notes(dto.getNotes())
                .build();

        deviceAreaRepository.save(assignment);

        return deviceMapper.toRegisterResponseDTO(
                savedDevice,
                dto.getDevicePassword(),
                area
        );
    }

    @Override
    @Transactional
    public DeviceAuthResponseDTO login(String deviceUsername, String devicePassword) {
        Device device = deviceRepository.findByDeviceUsername(deviceUsername)
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!device.getIsActive()) {
            throw new BadCredentialsException("Dispositivo deshabilitado");
        }

        if (!passwordEncoder.matches(devicePassword, device.getDevicePassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        DeviceArea currentAssignment = deviceAreaRepository
                .findCurrentAssignment(device.getId())
                .orElseThrow(() -> new DeviceNotAssignedException(device.getId()));

        device.setLastLogin(LocalDateTime.now());
        deviceRepository.save(device);

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        device.getDeviceUsername(),
                        device.getDevicePassword(),
                        List.of()
                )
        );

        List<AreaVideo> areaVideos = areaVideoRepository.findByAreaWithVideos(currentAssignment.getArea().getId());

        return deviceMapper.toAuthResponseDTO(
                device,
                currentAssignment,
                areaVideos,
                token
        );
    }

    @Override
    @Transactional
    public void reassign(Integer deviceId, DeviceAssignAreaDTO dto, Integer adminUserId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", deviceId));

        Area newArea = areaRepository.findById(dto.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área", dto.getAreaId()));

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", adminUserId));

        deviceAreaRepository.findCurrentAssignment(deviceId)
                .ifPresent(currentAssignment -> {
                    currentAssignment.setRemovedAt(LocalDateTime.now());
                    currentAssignment.setRemovedBy(admin);
                    deviceAreaRepository.save(currentAssignment);
                });

        DeviceArea newAssignment = DeviceArea.builder()
                .device(device)
                .area(newArea)
                .assignedBy(admin)
                .notes(dto.getNotes())
                .build();

        deviceAreaRepository.save(newAssignment);
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponseDTO getDeviceWithCurrentArea(Integer deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", deviceId));

        DeviceArea currentAssignment = deviceAreaRepository
                .findCurrentAssignment(deviceId)
                .orElse(null);

        return deviceMapper.toResponseDTO(device, currentAssignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceArea> getHistory(Integer deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Dispositivo", deviceId);
        }

        return deviceAreaRepository.findByDeviceIdOrderByAssignedAtDesc(deviceId);
    }

    @Override
    @Transactional
    public void deactivate(Integer deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", deviceId));

        device.setIsActive(false);
        deviceRepository.save(device);
    }

    @Override
    @Transactional
    public void updateLastSync(Integer deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", deviceId));

        device.setLastSync(LocalDateTime.now());
        deviceRepository.save(device);
    }
}
