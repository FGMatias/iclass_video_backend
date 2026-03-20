package com.iclass.video.dto.request.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBranchAdminDTO {
    @NotNull(message = "La sucursal es obligatoria")
    private Integer branchId;

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
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

    @Pattern(regexp = "^[0-9]{9,20}$")
    private String phone;
}
