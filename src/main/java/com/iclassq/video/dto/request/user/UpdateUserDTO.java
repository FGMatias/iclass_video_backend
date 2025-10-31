package com.iclassq.video.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
    private String username;

    @Size(min = 8, max = 100)
    private String password;

    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String paternalSurname;

    @Size(max = 50)
    private String maternalSurname;

    @Size(max = 20)
    private String documentNumber;

    @Email
    @Size(max = 100)
    private String email;

    @Pattern(regexp = "^[0-9]{9,20}$")
    private String phone;

    private Boolean isActive;
}
