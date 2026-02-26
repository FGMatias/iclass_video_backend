package com.iclass.video.dto.request.areavideo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAreaVideoDTO {
    @NotNull(message = "El orden es obligatorio")
    @Min(value = 0, message = "El orden debe ser mayor o igual a 0")
    private Integer orden;
}
