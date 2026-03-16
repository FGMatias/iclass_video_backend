package com.iclass.video.controller;

import com.iclass.video.dto.request.branch.CreateBranchDTO;
import com.iclass.video.dto.request.branch.UpdateBranchDTO;
import com.iclass.video.dto.response.branch.BranchResponseDTO;
import com.iclass.video.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branch")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<List<BranchResponseDTO>> findAll() {
        List<BranchResponseDTO> response = branchService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<BranchResponseDTO> findById(@PathVariable Integer id) {
        BranchResponseDTO response = branchService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<BranchResponseDTO> create(@RequestBody @Valid CreateBranchDTO dto) {
        BranchResponseDTO response = branchService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<BranchResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateBranchDTO dto
    ) {
        BranchResponseDTO response = branchService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<BranchResponseDTO> delete(@PathVariable Integer id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        branchService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        branchService.deactivate(id);
        return ResponseEntity.ok().build();
    }
}
