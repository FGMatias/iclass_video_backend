package com.iclass.video.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iclass.video.entity.PendingSyncEvent;
import com.iclass.video.entity.SystemConfig;
import com.iclass.video.entity.User;
import com.iclass.video.enums.SyncEventType;
import com.iclass.video.enums.TargetType;
import com.iclass.video.event.BranchConfigChangedEvent;
import com.iclass.video.event.DeviceDisabledEvent;
import com.iclass.video.event.DeviceReassignedEvent;
import com.iclass.video.event.PlayListChangedEvent;
import com.iclass.video.repository.PendingSyncEventRepository;
import com.iclass.video.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncEventListener {

    private final PendingSyncEventRepository pendingSyncEventRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @EventListener
    @Async
    @Transactional
    public void handleConfigChanged(BranchConfigChangedEvent event) {
        log.info("Evento: Configuración cambiada en sucursal {}", event.getBranchId());

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("configKey", event.getConfigKey());
        eventData.put("oldValue", event.getOldValue());
        eventData.put("newValue", event.getNewValue());

        createPendingEvent(
                SyncEventType.CONFIG_CHANGED,
                TargetType.BRANCH,
                event.getBranchId(),
                eventData,
                event.getUserId()
        );
    }

    @EventListener
    @Async
    @Transactional
    public void handlePlaylistChanged(PlayListChangedEvent event) {
        log.info("Evento: Playlist cambiada en área {}", event.getAreaId());

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("areaId", event.getAreaId());

        createPendingEvent(
                SyncEventType.PLAYLIST_CHANGED,
                TargetType.AREA,
                event.getAreaId(),
                eventData,
                event.getUserId()
        );
    }

    @EventListener
    @Async
    @Transactional
    public void handleDeviceReassigned(DeviceReassignedEvent event) {
        log.info("Evento: Dispositivo {} reasignado", event.getDeviceId());

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("oldAreaId", event.getOldAreaId());
        eventData.put("newAreaId", event.getNewAreaId());

        createPendingEvent(
                SyncEventType.DEVICE_REASSIGNED,
                TargetType.DEVICE,
                event.getDeviceId(),
                eventData,
                event.getUserId()
        );
    }

    @EventListener
    @Async
    @Transactional
    public void handleDeviceDisabled(DeviceDisabledEvent event) {
        log.info("Evento: Dispositivo {} desactivado", event.getDeviceId());

        createPendingEvent(
                SyncEventType.DEVICE_DISABLED,
                TargetType.DEVICE,
                event.getDeviceId(),
                null,
                event.getUserId()
        );
    }

    private void createPendingEvent(
            SyncEventType eventType,
            TargetType targetType,
            Integer targetId,
            Map<String, Object> eventData,
            Integer userId
    ) {
        try {
            User user = userId != null ? userRepository.findById(userId).orElse(null) : null;

            String eventDataJson = eventData != null ? objectMapper.writeValueAsString(eventData) : null;

            PendingSyncEvent pendingEvent = PendingSyncEvent.builder()
                    .eventType(eventType)
                    .targetType(targetType)
                    .targetId(targetId)
                    .eventData(eventDataJson)
                    .createdBy(user)
                    .build();

            pendingSyncEventRepository.save(pendingEvent);

            log.info("Evento pendiente creado: {} para {} {}", eventType, targetType, targetId);

        } catch (Exception e) {
            log.error("Error al crear evento pendiente", e);
        }
    }
}
