package com.iclass.video.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReassignBranchDTO {
    @NotNull(message = "La sucursal es obligatoria")
    private Integer branchId;
}
