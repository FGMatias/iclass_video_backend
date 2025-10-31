package com.iclassq.video.dto.response.area;

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
    private List<VideoInfo> videos;
    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class BranchInfo {
        private Integer id;
        private String name;
        private String companyName;
    }

    @Data
    @Builder
    public static class VideoInfo {
        private Integer id;
        private String name;
        private String urlVideo;
        private String thumbnail;
        private Integer duration;
        private Integer orden;
    }
}
