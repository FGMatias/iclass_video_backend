package com.iclass.video.controller;

import com.iclass.video.dto.request.video.UpdateVideoDTO;
import com.iclass.video.dto.response.video.VideoResponseDTO;
import com.iclass.video.dto.response.video.VideoUploadResponseDTO;
import com.iclass.video.security.SecurityUtils;
import com.iclass.video.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final SecurityUtils securityUtils;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<VideoUploadResponseDTO> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("companyId") Integer companyId,
            @RequestParam("name") String name,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        VideoUploadResponseDTO response = videoService.uploadVideo(file, companyId, name, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/stream")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> streamVideo(@PathVariable Integer id) {
        return videoService.streamVideo(id);
    }

    @GetMapping("/{id}/thumbnail")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> getThumbnail(@PathVariable Integer id) {
        return videoService.getThumbnail(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<List<VideoResponseDTO>> findAll() {
        List<VideoResponseDTO> response = videoService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<VideoResponseDTO> findById(@PathVariable Integer id) {
        VideoResponseDTO response = videoService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA', 'ADMINISTRADOR_SUCURSAL')")
    public ResponseEntity<List<VideoResponseDTO>> findByCompany(@PathVariable Integer companyId) {
        List<VideoResponseDTO> response = videoService.findByCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA')")
    public ResponseEntity<VideoResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateVideoDTO dto
    ) {
        VideoResponseDTO response = videoService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Integer userId = securityUtils.getCurrentUserId();
        videoService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA')")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        videoService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMINISTRADOR', 'ADMINISTRADOR_EMPRESA')")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        videoService.deactivate(id);
        return ResponseEntity.ok().build();
    }
}
