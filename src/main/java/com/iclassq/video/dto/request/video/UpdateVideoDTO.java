package com.iclassq.video.dto.request.video;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVideoDTO {
    @Size(max = 200)
    private String name;

    @Size(max = 500)
    private String urlVideo;

    @Min(value = 1)
    private Integer duration;

    @Size(max = 500)
    private String thumbnail;

    private Boolean isActive;
}
