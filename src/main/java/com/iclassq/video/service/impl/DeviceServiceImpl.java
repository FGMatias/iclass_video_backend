package com.iclassq.video.service.impl;

import com.iclassq.video.dto.request.device.DeviceAssignAreaDTO;
import com.iclassq.video.dto.request.device.RegisterDeviceDTO;
import com.iclassq.video.dto.response.device.DeviceAuthResponseDTO;
import com.iclassq.video.dto.response.device.DeviceRegisterResponseDTO;
import com.iclassq.video.dto.response.device.DeviceResponseDTO;
import com.iclassq.video.dto.response.video.VideoSimpleDTO;
import com.iclassq.video.entity.*;
import com.iclassq.video.exception.DeviceNotAssignedException;
import com.iclassq.video.exception.DuplicateEntityException;
import com.iclassq.video.exception.ResourceNotFoundException;
import com.iclassq.video.repository.*;
import com.iclassq.video.security.JwtService;
import com.iclassq.video.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceAreaRepository deviceAreaRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AreaVideoRepository areaVideoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

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

        Device device = Device.builder()
                .user(admin)
                .deviceType(deviceType)
                .deviceName(dto.getDeviceName())
                .deviceIdentifier(dto.getDeviceIdentifier())
                .deviceUsername(dto.getDeviceUsername())
                .devicePassword(hashedPassword)
                .isActive(true)
                .build();

        Device savedDevice = deviceRepository.save(device);

        DeviceArea assignment = DeviceArea.builder()
                .device(savedDevice)
                .area(area)
                .assignedBy(admin)
                .notes(dto.getNotes())
                .build();

        deviceAreaRepository.save(assignment);

        return DeviceRegisterResponseDTO.builder()
                .id(savedDevice.getId())
                .deviceName(savedDevice.getDeviceName())
                .deviceUsername(savedDevice.getDeviceUsername())
                .devicePassword(savedDevice.getDevicePassword())
                .deviceType(deviceType.getName())
                .areaId(area.getId())
                .areaName(area.getName())
                .message("Dispositivo registrado correctamente.")
                .build();
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

        Area area = currentAssignment.getArea();
        Branch branch = area.getBranch();
        Company company = branch.getCompany();

        List<AreaVideo> areaVideos = areaVideoRepository.findByAreaWithVideos(area.getId());

        List<VideoSimpleDTO> playlist = areaVideos.stream()
                .sorted(Comparator.comparing(AreaVideo::getOrden))
                .map(av -> VideoSimpleDTO.builder()
                        .id(av.getVideo().getId())
                        .name(av.getVideo().getName())
                        .urlVideo(av.getVideo().getUrlVideo())
                        .thumbnail(av.getVideo().getThumbnail())
                        .duration(av.getVideo().getDuration())
                        .orden(av.getOrden())
                        .build())
                .collect(Collectors.toList());

        return DeviceAuthResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .id(device.getId())
                .deviceName(device.getDeviceName())
                .deviceUsername(device.getDeviceUsername())
                .deviceType(device.getDeviceType().getName())
                .areaId(area.getId())
                .areaName(area.getName())
                .branchId(branch.getId())
                .branchName(branch.getName())
                .companyId(company.getId())
                .companyName(company.getName())
                .config(DeviceAuthResponseDTO.DeviceConfig.builder()
                        .autoSyncInterval(21600)
                        .autoPlay(true)
                        .loopPlayList(true)
                        .volume(80)
                        .build())
                .playlist(playlist)
                .lastSync(device.getLastSync())
                .build();
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

        DeviceResponseDTO.DeviceResponseDTOBuilder builder = DeviceResponseDTO.builder()
                .id(device.getId())
                .deviceName(device.getDeviceName())
                .deviceIdentifier(device.getDeviceIdentifier())
                .deviceUsername(device.getDeviceUsername())
                .deviceType(device.getDeviceType().getName())
                .isActive(device.getIsActive())
                .lastLogin(device.getLastLogin())
                .lastSync(device.getLastSync())
                .createdAt(device.getCreatedAt());

        if (currentAssignment != null) {
            Area area = currentAssignment.getArea();
            Branch branch = area.getBranch();
            Company company = branch.getCompany();

            builder
                    .currentAreaId(area.getId())
                    .currentAreaName(area.getName())
                    .currentBranchName(branch.getName())
                    .currentCompanyName(company.getName())
                    .assignedAt(currentAssignment.getAssignedAt());
        }

        if (device.getUser() != null) {
            builder.configuredByUsername(device.getUser().getUsername());
        }

        return builder.build();
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
