package com.iclassq.video.dto.response.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoSimpleDTO {
    private Integer id;
    private String name;
    private String urlVideo;
    private String thumbnail;
    private Integer duration;

    private Integer orden;
    private Long fileSize;
    private String checksum;
}
