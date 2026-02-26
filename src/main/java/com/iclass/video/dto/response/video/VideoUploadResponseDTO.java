package com.iclass.video.dto.response.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoUploadResponseDTO {
    private Integer id;
    private Integer companyId;
    private String companyName;
    private String name;
    private String urlVideo;
    private String thumbnail;
    private Integer duration;
    private Long fileSize;
    private String fileExtension;
    private String checksum;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
