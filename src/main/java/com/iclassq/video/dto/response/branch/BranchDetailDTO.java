package com.iclassq.video.dto.response.branch;

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
public class BranchDetailDTO {
    private Integer id;
    private String name;
    private String direction;
    private String phone;
    private Boolean isActive;
    private CompanySimpleDTO company;
    private Integer totalAreas;
    private List<UserSimpleDTO> administrators;
    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class CompanySimpleDTO {
        private Integer id;
        private String name;
    }
}
