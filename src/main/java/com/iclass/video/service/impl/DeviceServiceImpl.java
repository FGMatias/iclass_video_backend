package com.iclass.video.service.impl;

import com.iclass.video.dto.request.device.DeviceAssignAreaDTO;
import com.iclass.video.dto.request.device.CreateDeviceDTO;
import com.iclass.video.dto.request.device.UpdateDeviceDTO;
import com.iclass.video.dto.response.device.DeviceAuthResponseDTO;
import com.iclass.video.dto.response.device.DeviceResponseDTO;
import com.iclass.video.dto.response.device.DeviceSyncResponseDTO;
import com.iclass.video.entity.*;
import com.iclass.video.event.DeviceDisabledEvent;
import com.iclass.video.event.DeviceReassignedEvent;
import com.iclass.video.repository.*;
import com.iclass.video.entity.*;
import com.iclass.video.exception.DeviceNotAssignedException;
import com.iclass.video.exception.DuplicateEntityException;
import com.iclass.video.exception.ResourceNotFoundException;
import com.iclass.video.mapper.DeviceMapper;
import com.iclass.video.repository.*;
import com.iclass.video.security.JwtService;
import com.iclass.video.service.BranchConfigService;
import com.iclass.video.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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
    private final BranchRepository branchRepository;
    private final AreaRepository areaRepository;
    private final AreaVideoRepository areaVideoRepository;
    private final UserRepository userRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DeviceMapper deviceMapper;
    private final BranchConfigService branchConfigService;
    private final DeviceSyncEventRepository deviceSyncEventRepository;
    private final PendingSyncEventRepository pendingSyncEventRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.default-password}")
    private String defaultPassword;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> findAll() {
        List<Device> devices = deviceRepository.findAll();
        return deviceMapper.toResponseDTOList(devices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> findByAreaId(Integer areaId) {
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area", areaId);
        }

        List<DeviceArea> assignments = deviceAreaRepository.findCurrentByAreaId(areaId);
        return assignments.stream()
                .map(da -> deviceMapper.toResponseDTO(da.getDevice(), da))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> findByBranchId(Integer branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new ResourceNotFoundException("Sucursal", branchId);
        }

        List<DeviceArea> assignments = deviceAreaRepository.findCurrentByBranchId(branchId);
        return assignments.stream()
                .map(da -> deviceMapper.toResponseDTO(da.getDevice(), da))
                .toList();
    }

    @Override
    @Transactional
    public DeviceResponseDTO create(CreateDeviceDTO dto, Integer adminUserId) {
        if (deviceRepository.existsByDeviceUsername(dto.getDeviceUsername())) {
            throw new DuplicateEntityException("Dispositivo", "username", dto.getDeviceUsername());
        }

        Area area = areaRepository.findById(dto.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área", dto.getAreaId()));

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", adminUserId));

        DeviceType deviceType = deviceTypeRepository.findById(dto.getDeviceTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de dispositivo", dto.getDeviceTypeId()));

        Branch branch = area.getBranch();

        String hashedPassword = passwordEncoder.encode(defaultPassword);

        Device device = deviceMapper.toEntity(dto, deviceType, admin, branch, hashedPassword);
        Device savedDevice = deviceRepository.save(device);

        DeviceArea assignment = DeviceArea.builder()
                .device(savedDevice)
                .area(area)
                .assignedBy(admin)
                .build();

        deviceAreaRepository.save(assignment);

        return deviceMapper.toResponseDTO(savedDevice, assignment);
    }

    @Override
    public DeviceResponseDTO update(Integer id, UpdateDeviceDTO dto) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));

        if (dto.getDeviceUsername() != null && !dto.getDeviceUsername().equals(device.getDeviceUsername())) {
            if (deviceRepository.existsByDeviceUsername(dto.getDeviceUsername())) {
                throw new DuplicateEntityException("Dispositivo", "username", dto.getDeviceUsername());
            }
        }

        DeviceType deviceType = null;
        if (dto.getDeviceTypeId() != null) {
            deviceType = deviceTypeRepository.findById(dto.getDeviceTypeId())
                        .orElseThrow(() -> new ResourceNotFoundException("Tipo de dispositivo", dto.getDeviceTypeId()));
        }

        deviceMapper.updateEntity(device, dto, deviceType);
        Device updatedDevice = deviceRepository.save(device);

        DeviceArea currentAssignment = deviceAreaRepository
                .findCurrentAssignment(id)
                .orElse(null);

        return deviceMapper.toResponseDTO(updatedDevice, currentAssignment);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!deviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dispositivo", id);
        }

        deviceRepository.deleteById(id);
    }

    @Override
    public void activate(Integer id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));

        device.setIsActive(true);
        deviceRepository.save(device);
    }

    @Override
    @Transactional
    public void deactivate(Integer id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));

        device.setIsActive(false);
        deviceRepository.save(device);

        eventPublisher.publishEvent(new DeviceDisabledEvent(id, null));
    }

    @Override
    @Transactional
    public void reassign(Integer id, DeviceAssignAreaDTO dto, Integer adminUserId) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));

        Area newArea = areaRepository.findById(dto.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área", dto.getAreaId()));

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", adminUserId));

        DeviceArea currentAssignment = deviceAreaRepository
                .findCurrentAssignment(id)
                .orElseThrow(() -> new DeviceNotAssignedException(id));

        Integer oldAreaId = currentAssignment.getArea().getId();

        currentAssignment.setRemovedAt(LocalDateTime.now());
        currentAssignment.setRemovedBy(admin);
        deviceAreaRepository.save(currentAssignment);

        DeviceArea newAssignment = DeviceArea.builder()
                .device(device)
                .area(newArea)
                .assignedBy(admin)
                .build();

        deviceAreaRepository.save(newAssignment);

        eventPublisher.publishEvent(new DeviceReassignedEvent(
                id,
                oldAreaId,
                dto.getAreaId(),
                adminUserId
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponseDTO getDeviceWithCurrentArea(Integer id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));

        DeviceArea currentAssignment = deviceAreaRepository
                .findCurrentAssignment(id)
                .orElse(null);

        return deviceMapper.toResponseDTO(device, currentAssignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceArea> getHistory(Integer id) {
        if (!deviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dispositivo", id);
        }

        return deviceAreaRepository.findByDeviceIdOrderByAssignedAtDesc(id);
    }

    @Override
    @Transactional
    public DeviceSyncResponseDTO syncDevice(Integer id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));

        DeviceArea currentAssignment = deviceAreaRepository
                .findCurrentAssignment(id)
                .orElseThrow(() -> new DeviceNotAssignedException(id));

        Integer branchId = device.getBranch().getId();
        Integer areaId = currentAssignment.getArea().getId();

        Long lastEventId = deviceSyncEventRepository.findLastProcessedEventId(id).orElse(0L);

        List<PendingSyncEvent> pendingEvents = pendingSyncEventRepository.findPendingEventsForDevice(
                lastEventId,
                id,
                branchId,
                areaId
        );

        pendingEvents.forEach(event -> {
            DeviceSyncEvent deviceSyncEvent = DeviceSyncEvent.builder()
                    .device(device)
                    .event(event)
                    .build();
            deviceSyncEventRepository.save(deviceSyncEvent);
        });

        Integer volume = branchConfigService.getConfigValueAsInt(branchId, "device.default.volume");
        Integer syncInterval = branchConfigService.getConfigValueAsInt(branchId, "device.sync.interval.seconds");
        Boolean autoPlay = branchConfigService.getConfigValueAsBoolean(branchId, "device.auto.play");
        Boolean loopPlaylist = branchConfigService.getConfigValueAsBoolean(branchId, "device.loop.playlist");

        List<AreaVideo> areaVideos = areaVideoRepository.findByAreaWithVideos(areaId);

        device.setLastSync(LocalDateTime.now());
        deviceRepository.save(device);

        return deviceMapper.toSyncResponseDTO(
                areaVideos,
                volume,
                syncInterval,
                autoPlay,
                loopPlaylist,
                pendingEvents
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

        Integer branchId = currentAssignment.getArea().getBranch().getId();

        Integer volume = branchConfigService.getConfigValueAsInt(branchId, "device.default.volume");
        Integer syncInterval = branchConfigService.getConfigValueAsInt(branchId, "device.sync.interval.seconds");
        Boolean autoPlay = branchConfigService.getConfigValueAsBoolean(branchId, "device.auto.play");
        Boolean loopPlaylist = branchConfigService.getConfigValueAsBoolean(branchId, "device.loop.playlist");

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
                token,
                volume,
                syncInterval,
                autoPlay,
                loopPlaylist
        );
    }

    @Override
    @Transactional
    public void resetPassword(Integer id, String newPassword) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));

        String hashedPassword = passwordEncoder.encode(newPassword);
        device.setDevicePassword(hashedPassword);
        deviceRepository.save(device);
    }
}
