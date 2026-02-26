package com.iclass.video.controller;

import com.iclass.video.dto.request.areavideo.CreateAreaVideoDTO;
import com.iclass.video.dto.request.areavideo.UpdateAreaVideoDTO;
import com.iclass.video.dto.response.areavideo.AreaVideoResponseDTO;
import com.iclass.video.security.SecurityUtils;
import com.iclass.video.service.AreaVideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/area-video")
@RequiredArgsConstructor
public class AreaVideoController {

    private final AreaVideoService areaVideoService;
    private final SecurityUtils securityUtils;

    @GetMapping("/area/{areaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<List<AreaVideoResponseDTO>> findByArea(@PathVariable Integer areaId) {
        List<AreaVideoResponseDTO> response = areaVideoService.findByArea(areaId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<AreaVideoResponseDTO> create(
            @RequestBody @Valid CreateAreaVideoDTO dto
    ) {
        Integer userId = securityUtils.getCurrentUserId();
        AreaVideoResponseDTO response = areaVideoService.create(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<AreaVideoResponseDTO> updateOrder(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateAreaVideoDTO dto
    ) {
        Integer userId = securityUtils.getCurrentUserId();
        AreaVideoResponseDTO response = areaVideoService.updateOrder(id, dto, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Integer userId = securityUtils.getCurrentUserId();
        areaVideoService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
