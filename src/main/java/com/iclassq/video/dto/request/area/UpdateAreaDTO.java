package com.iclassq.video.dto.request.area;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAreaDTO {
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    private Boolean isActive;
}
