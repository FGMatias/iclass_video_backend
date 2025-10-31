package com.iclassq.video.dto.request.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
    @NotNull(message = "El rol es obligatorio")
    private Integer roleId;

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username solo puede contener letras, números, puntos, guiones")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String paternalSurname;

    @Size(max = 50)
    private String maternalSurname;

    @Size(max = 20)
    private String documentNumber;

    @NotBlank(message = "El email es obligatorio")
    @Email
    @Size(max = 100)
    private String email;

    @Pattern(regexp = "^[0-9]{9,20}$", message = "Teléfono inválido")
    private String phone;
}
