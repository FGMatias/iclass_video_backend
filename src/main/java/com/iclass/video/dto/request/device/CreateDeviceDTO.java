package com.iclass.video.dto.request.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeviceDTO {
    @NotBlank(message = "El nombre del dispositivo es obligatorio")
    @Size(max = 100)
    private String deviceName;

    @NotNull(message = "El tipo de dispositivo es obligatorio")
    private Integer deviceTypeId;

    private Integer areaId;

    @NotBlank(message = "El username es obligatorio")
    @Size(
        min = 4,
        max = 50,
        message = "Username debe tener entre 4 y 50 caracteres"
    )
    @Pattern(
        regexp = "^[a-zA-Z0-9_-]+$",
        message = "Username solo puede contener letras, números, guiones y guiones bajos"
    )
    private String deviceUsername;

    private String notes;
}
