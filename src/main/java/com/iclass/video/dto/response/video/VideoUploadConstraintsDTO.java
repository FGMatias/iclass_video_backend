package com.iclass.video.dto.response.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoUploadConstraintsDTO {
    private Integer maxSizeMb;
    private List<String> allowedExtensions;
}
