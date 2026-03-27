package com.iclass.video.dto.request.areavideo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncPlaylistDTO {
    @NotNull(message = "La lista de videos es obligatoria")
    private List<Integer> videoIds;
}
