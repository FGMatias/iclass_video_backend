package com.iclassq.video.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private Integer userId;
    private String username;
    private String name;
    private String email;
    private Integer roleId;
    private String roleName;
}
