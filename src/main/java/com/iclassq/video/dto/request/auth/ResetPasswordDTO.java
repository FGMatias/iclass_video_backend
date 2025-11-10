package com.iclassq.video.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO {
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener el menos 8 caracteres")
    private String newPassword;
}
