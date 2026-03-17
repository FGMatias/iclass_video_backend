package com.iclass.video.dto.request.config;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSystemConfigDTO {
    @NotBlank(message = "El valor es obligatorio")
    private String configValue;
}
