package com.iclass.video.dto.request.areavideo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAreaVideoDTO {
    @NotNull(message = "El área es obligatoria")
    private Integer areaId;

    @NotNull(message = "El video es obligatorio")
    private Integer videoId;

    @NotNull(message = "El orden es obligatorio")
    @Min(value = 0, message = "El orden debe ser mayor a igual a 0")
    private Integer orden;
}
