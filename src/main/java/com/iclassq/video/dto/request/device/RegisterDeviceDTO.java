package com.iclassq.video.dto.request.device;

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
public class RegisterDeviceDTO {
    @NotBlank(message = "El nombre del dispositivo es obligatorio")
    @Size(max = 100)
    private String deviceName;

    @Size(max = 100)
    private String deviceIdentifier;

    @NotNull(message = "El tipo de dispositivo es obligatorio")
    private Integer deviceTypeId;

    @NotNull(message = "El área es obligatoria")
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

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(
        min = 6,
        max = 100,
        message = "Password debe tener al menos 6 caracteres"
    )
    private String devicePassword;

    private String notes;
}
