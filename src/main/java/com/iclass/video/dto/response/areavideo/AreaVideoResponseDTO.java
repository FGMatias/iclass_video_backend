package com.iclass.video.dto.response.areavideo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaVideoResponseDTO {
    private Integer id;
    private Integer videoId;
    private String videoName;
    private String videoThumbnail;
    private Integer videoDuration;
    private Integer areaId;
    private String areaName;
    private Integer orden;
    private LocalDateTime createdAt;
}
