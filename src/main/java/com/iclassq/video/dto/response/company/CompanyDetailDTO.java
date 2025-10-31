package com.iclassq.video.dto.response.company;

import com.iclassq.video.dto.response.user.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDetailDTO {
    private Integer id;
    private String name;
    private String ruc;
    private String direction;
    private String phone;
    private String email;
    private String logo;
    private Boolean isActive;
    private Integer totalBranches;
    private Integer totalVideos;
    private List<UserSimpleDTO> administrators;
    private LocalDateTime createdAt;
}
