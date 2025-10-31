package com.iclassq.video.dto.request.video;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVideoDTO {
    @NotNull(message = "La empresa es obligatoria")
    private Integer companyId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200)
    private String name;

    @NotBlank(message = "La URL del video es obligatoria")
    @Size(max = 500)
    private String urlVideo;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private Integer duration;

    @Size(max = 500)
    private String thumbnail;
}
