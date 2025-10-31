package com.iclassq.video.dto.request.area;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignVideosDTO {
    @NotEmpty(message = "Debe incluir al menos un video")
    private List<VideoAssignment> videos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoAssignment {
        @NotNull(message = "El video es obligatorio")
        private Integer videoId;

        @Min(value = 1, message = "El orden debe ser mayor a 0")
        private Integer orden;
    }
}
