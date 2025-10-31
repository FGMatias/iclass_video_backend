package com.iclassq.video.dto.request.device;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeviceDTO {
    @NotNull(message = "El usuario es obligatorio")
    private Integer userId;

    @NotNull(message = "El área es obligatoria")
    private Integer areaId;

    @Size(max = 100)
    private String deviceName;

    @Size(max = 100)
    private String deviceIdentifier;
}
