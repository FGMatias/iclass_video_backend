package com.iclassq.video.dto.request.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String name;

    @Pattern(regexp = "^[0-9]{11}$", message = "RUC debe tener 11 dígitos")
    private String ruc;

    @Size(max = 500)
    private String direction;

    @Pattern(regexp = "^[0-9]{9,20}$")
    private String phone;

    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 500)
    private String logo;
}
