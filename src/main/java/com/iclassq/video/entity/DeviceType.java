package com.iclassq.video.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "device_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 150)
    private String name;
}
