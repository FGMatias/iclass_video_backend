package com.iclass.video.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private String name;
    private String email;
    private Integer roleId;
    private String roleName;
    private LocalDateTime expiresAt;
}
