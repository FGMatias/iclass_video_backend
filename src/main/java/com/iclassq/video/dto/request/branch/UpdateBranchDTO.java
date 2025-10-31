package com.iclassq.video.dto.request.branch;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBranchDTO {
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String direction;

    @Pattern(regexp = "^[0-9]{9,20}$")
    private String phone;

    private Boolean isActive;
}
