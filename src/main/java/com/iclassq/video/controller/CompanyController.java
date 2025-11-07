package com.iclassq.video.controller;

import com.iclassq.video.dto.request.company.CreateCompanyDTO;
import com.iclassq.video.dto.request.company.UpdateCompanyDTO;
import com.iclassq.video.dto.response.company.CompanyResponseDTO;
import com.iclassq.video.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> findAll() {
        List<CompanyResponseDTO> response = companyService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> findById(@PathVariable Integer id) {
        CompanyResponseDTO response = companyService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> create(@RequestBody @Valid CreateCompanyDTO dto) {
        CompanyResponseDTO response = companyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateCompanyDTO dto
    ) {
        CompanyResponseDTO response = companyService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> delete(@PathVariable Integer id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        companyService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        companyService.deactivate(id);
        return ResponseEntity.ok().build();
    }
}
