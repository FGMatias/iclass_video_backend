package com.iclassq.video.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "device")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_type_id", referencedColumnName = "id")
    private DeviceType deviceType;

    @Column(name = "device_name", nullable = false, length = 100)
    private String deviceName;

    @Column(name = "device_identifier", unique = true, nullable = false, length = 100, updatable = false)
    private String deviceIdentifier;

    @Column(name = "device_username", unique = true, nullable = false, length = 100)
    private String deviceUsername;

    @Column(name = "device_password", nullable = false, length = 255)
    private String devicePassword;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "last_sync")
    private LocalDateTime lastSync;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (deviceIdentifier == null || deviceIdentifier.isEmpty()) {
            deviceIdentifier = generateDeviceIdentifier();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateDeviceIdentifier() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return String.format("DEV-%s-%s", timestamp, uuid);
    }
}
