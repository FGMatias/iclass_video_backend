package com.iclass.video.controller;

import com.iclass.video.dto.request.company.CreateCompanyDTO;
import com.iclass.video.dto.request.company.UpdateCompanyDTO;
import com.iclass.video.dto.response.company.CompanyDetailDTO;
import com.iclass.video.dto.response.company.CompanyResponseDTO;
import com.iclass.video.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<List<CompanyResponseDTO>> findAll() {
        List<CompanyResponseDTO> response = companyService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<CompanyResponseDTO> findById(@PathVariable Integer id) {
        CompanyResponseDTO response = companyService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/detail")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<CompanyDetailDTO> detail(@PathVariable Integer id) {
        CompanyDetailDTO response = companyService.detail(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<CompanyResponseDTO> create(@RequestBody @Valid CreateCompanyDTO dto) {
        CompanyResponseDTO response = companyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<CompanyResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateCompanyDTO dto
    ) {
        CompanyResponseDTO response = companyService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<CompanyResponseDTO> delete(@PathVariable Integer id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        companyService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR')")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        companyService.deactivate(id);
        return ResponseEntity.ok().build();
    }
}
