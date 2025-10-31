package com.iclassq.video.dto.response.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoResponseDTO {
    private Integer id;
    private Integer companyId;
    private String companyName;
    private String name;
    private String urlVideo;
    private Integer duration;
    private String thumbnail;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
