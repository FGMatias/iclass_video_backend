package com.iclassq.video.dto.response.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoSimpleDTO {
    private Integer id;
    private String name;
    private String urlVideo;
    private String thumbnail;
    private Integer duration;
}
