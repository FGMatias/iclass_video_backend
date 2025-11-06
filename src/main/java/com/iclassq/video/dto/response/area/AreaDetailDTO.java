package com.iclassq.video.dto.response.area;

import com.iclassq.video.dto.response.video.VideoSimpleDTO;
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
public class AreaDetailDTO {
    private Integer id;
    private String name;
    private String description;
    private Boolean isActive;
    private BranchInfo branch;
    private List<VideoSimpleDTO> videos;
    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BranchInfo {
        private Integer id;
        private String name;
        private String companyName;
    }
}
