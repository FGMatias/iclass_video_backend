package com.iclassq.video.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @Column(name = "username", unique = true, length = 100)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "paternal_surname", length = 100)
    private String paternalSurname;

    @Column(name = "maternal_surname", length = 100)
    private String maternalSurname;

    @Column(name = "document_number", length = 100)
    private String documentNumber;

    @Column(name = "email", unique = true, columnDefinition = "TEXT")
    private String email;

    @Column(name = "phone", length = 9)
    private String phone;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
