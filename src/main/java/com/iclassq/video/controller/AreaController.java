package com.iclassq.video.controller;

import com.iclassq.video.dto.request.area.CreateAreaDTO;
import com.iclassq.video.dto.request.area.UpdateAreaDTO;
import com.iclassq.video.dto.response.area.AreaResponseDTO;
import com.iclassq.video.service.AreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/area")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<List<AreaResponseDTO>> findAll() {
        List<AreaResponseDTO> response = areaService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<AreaResponseDTO> findById(@PathVariable Integer id) {
        AreaResponseDTO response = areaService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<AreaResponseDTO> create(@RequestBody @Valid CreateAreaDTO dto) {
        AreaResponseDTO response = areaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<AreaResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateAreaDTO dto
    ) {
        AreaResponseDTO response = areaService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<AreaResponseDTO> delete(@PathVariable Integer id) {
        areaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        areaService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        areaService.deactivate(id);
        return ResponseEntity.ok().build();
    }
}
