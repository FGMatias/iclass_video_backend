package com.iclass.video.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_area")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", referencedColumnName = "id")
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_user_id", referencedColumnName = "id")
    private User assignedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "removed_by_user_id", referencedColumnName = "id")
    private User removedBy;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Column(name = "is_current", insertable = false, updatable = false)
    private Boolean isCurrent;

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }
}
