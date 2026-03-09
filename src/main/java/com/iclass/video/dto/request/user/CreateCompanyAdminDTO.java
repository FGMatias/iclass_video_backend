package com.iclass.video.dto.request.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyAdminDTO {
    @NotNull(message = "La empresa es obligatoria")
    private Integer companyId;

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50)
    @Pattern(
        regexp = "^[a-zA-Z0-9._-]+$",
        message = "Username solo puede contener letras, números, puntos, guiones"
    )
    private String username;

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
