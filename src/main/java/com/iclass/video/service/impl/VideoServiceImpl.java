package com.iclass.video.service.impl;

import com.iclass.video.dto.request.video.UpdateVideoDTO;
import com.iclass.video.dto.response.video.VideoResponseDTO;
import com.iclass.video.dto.response.video.VideoUploadResponseDTO;
import com.iclass.video.entity.Company;
import com.iclass.video.entity.Video;
import com.iclass.video.event.PlayListChangedEvent;
import com.iclass.video.exception.BadRequestException;
import com.iclass.video.exception.ResourceNotFoundException;
import com.iclass.video.mapper.VideoMapper;
import com.iclass.video.repository.AreaVideoRepository;
import com.iclass.video.repository.CompanyRepository;
import com.iclass.video.repository.VideoRepository;
import com.iclass.video.service.SystemConfigService;
import com.iclass.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final CompanyRepository companyRepository;
    private final AreaVideoRepository areaVideoRepository;
    private final SystemConfigService systemConfigService;
    private final VideoMapper videoMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public VideoUploadResponseDTO uploadVideo(
            MultipartFile file,
            Integer companyId,
            String name,
            MultipartFile thumbnail
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", companyId));

        validateVideoFile(file);

        Video video = videoMapper.toEntityForUpload(name, company);
        Video savedVideo = videoRepository.save(video);

        try {
            String basePath = systemConfigService.getConfigValue("video.storage.path");
            String companyFolder = buildCompanyFolder(company);
            String videoDir = basePath + "/" + companyFolder + "/" + savedVideo.getId();

            Files.createDirectories(Paths.get(videoDir));
            log.info("Directorio creado {}", videoDir);

            String extension = getFileExtension(file.getOriginalFilename());
            String fileName = "video" + extension;
            String fullPath = videoDir + "/" + fileName;
            Files.copy(file.getInputStream(), Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);
            log.info("Video guardado {}", fullPath);

            String checksum = calculateChecksum(fullPath);
            Long fileSize = file.getSize();
            Integer duration = extractDuration(fullPath);

            String thumbnailUrl = processThumbnail(thumbnail, fullPath, videoDir, savedVideo.getId());

            String relativePath = companyFolder + "/" + savedVideo.getId() + "/" + fileName;
            savedVideo.setFilePath(relativePath);
            savedVideo.setFileName(fileName);
            savedVideo.setFileSize(fileSize);
            savedVideo.setFileExtension(extension);
            savedVideo.setChecksum(checksum);
            savedVideo.setDuration(duration);
            savedVideo.setUrlVideo("/api/video/" + savedVideo.getId() + "/stream");
            savedVideo.setThumbnail(thumbnailUrl);

            videoRepository.save(savedVideo);
            log.info("Video subido exitosamente: id={}, name={}, size={}MB, duration={}s",
                    savedVideo.getId(), name, fileSize / (1024 * 1024), duration);

            return videoMapper.toUploadResponseDTO(savedVideo);
        } catch (Exception e) {
            videoRepository.delete(savedVideo);
            log.error("Error al subir video, registro eliminado: {}", e.getMessage(), e);
            throw new BadRequestException("Error al subir el video: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> streamVideo(Integer videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video", videoId));

        if (video.getFilePath() == null) {
            throw new BadRequestException("El video no tiene archivo asociado");
        }

        String basePath = systemConfigService.getConfigValue("video.storage.path");
        String fullPath = basePath + "/" + video.getFilePath();

        try {
            Path path = Paths.get(fullPath);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Archivo de video no encontrado en disco");
            }

            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + video.getFileName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(video.getFileSize()))
                    .body(resource);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al servir video id={}: {}", videoId, e.getMessage());
            throw new BadRequestException("Error al reproducir el video");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> getThumbnail(Integer videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video", videoId));

        if (video.getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }

        String basePath = systemConfigService.getConfigValue("video.storage.path");
        Path filePath = Paths.get(video.getFilePath());
        String directory = filePath.getParent().toString();
        String thumbnailPath = basePath + "/" + directory + "/thumbnail.jpg";

        try {
            Path path = Paths.get(thumbnailPath);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error al servir thumbnail del video id={}: {}", videoId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<VideoResponseDTO> findAll() {
        List<Video> videos = videoRepository.findAll();
        return videoMapper.toResponseDTOList(videos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VideoResponseDTO> findByCompany(Integer companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Empresa", companyId);
        }

        List<Video> videos = videoRepository.findByCompany_Id(companyId);
        return videoMapper.toResponseDTOList(videos);
    }

    @Override
    @Transactional(readOnly = true)
    public VideoResponseDTO findById(Integer id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", id));
        return videoMapper.toResponseDTO(video);
    }

    @Override
    @Transactional
    public VideoResponseDTO update(Integer id, UpdateVideoDTO dto) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", id));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            video.setName(dto.getName());
        }

        videoRepository.save(video);
        log.info("Video actualizado: id={}, name={}", id, video.getName());

        return videoMapper.toResponseDTO(video);
    }

    @Override
    @Transactional
    public void delete(Integer id, Integer userId) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", id));

        List<Integer> affectedAreaIds = areaVideoRepository.findAreaIdsByVideoId(id);

        deleteVideoDirectory(video);

        videoRepository.delete(video);
        log.info("Video eliminado: id={}, name={}", id, video.getName());

        affectedAreaIds.forEach(areaId -> {
            eventPublisher.publishEvent(new PlayListChangedEvent(areaId, userId));
            log.info("PlaylistChangedEvent publicado para área {}", areaId);
        });
    }

    @Override
    @Transactional
    public void activate(Integer id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", id));
        video.setIsActive(true);
        videoRepository.save(video);
        log.info("Video activado: id={}", id);
    }

    @Override
    public void deactivate(Integer id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", id));
        video.setIsActive(false);
        videoRepository.save(video);
        log.info("Video desactivado: id={}", id);
    }

    private String buildCompanyFolder(Company company) {
        return company.getId() + "_" + sanitizeCompanyName(company.getName());
    }

    private String sanitizeCompanyName(String name) {
        if (name == null || name.isEmpty()) {
            return "sin_nombre";
        }
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]", "_")
                .replaceAll("_{2,}", "_")
                .replaceAll("^_+|_+$", "");
    }

    private void validateVideoFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("El archivo de video es obligatorio");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String allowedExtensions = systemConfigService.getConfigValue("video.allowed.extensions");
        List<String> allowed = Arrays.asList(allowedExtensions.split(","));

        if (!allowed.contains(extension.toLowerCase())) {
            throw new BadRequestException("Extensión no permitida: " + extension + ". Permitidas: " + allowedExtensions);
        }

        Integer maxSizeMB = systemConfigService.getConfigValueAsInt("video.max.size.mb");
        long maxSizeBytes = (long) maxSizeMB * 1024 * 1024;

        if (file.getSize() > maxSizeBytes) {
            throw new BadRequestException("El archivo excede el tamaño máximo permitido de " + maxSizeMB + " MB");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new BadRequestException("Archivo sin extensión válida");
        }

        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }

    private String calculateChecksum(String filePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (FileInputStream fis = new FileInputStream(filePath)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }

            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("Error calculando checksum: {}", e.getMessage());
            throw new BadRequestException("Error al calcular checksum del archivo");
        }
    }

    private Integer extractDuration(String filePath) {
        try {
            MultimediaObject multimediaObject = new MultimediaObject(new File(filePath));
            MultimediaInfo info = multimediaObject.getInfo();
            int durationSeconds = (int) (info.getDuration() / 1000);
            log.debug("Duración extraída: {}s para {}", durationSeconds, filePath);

            return durationSeconds;
        } catch (Exception e) {
            log.warn("No se pudo extraer duración del video: {}", e.getMessage());
            return 0;
        }
    }

    private void generateThumbnail(String videoPath, String thumbnailPath) {
        try {
            MultimediaObject source = new MultimediaObject(new File(videoPath));
            VideoAttributes videoAttributes = new VideoAttributes();
            videoAttributes.setCodec("mjpeg");
            videoAttributes.setSize(new VideoSize(320, 180));

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("image2");
            attrs.setOffset(2.0f);
            attrs.setDuration(0.001f);
            attrs.setVideoAttributes(videoAttributes);

            Encoder encoder = new Encoder();
            encoder.encode(source, new File(thumbnailPath), attrs);

            log.info("Thumbnail generado automáticamente: {}", thumbnailPath);
        } catch (Exception e) {
            log.warn("No se pudo generar thumbnail automáticamente: {}", e.getMessage());
        }
    }

    private String processThumbnail(MultipartFile thumbnailFile, String videoFullPath,
                                    String videoDir, Integer videoId) throws IOException {
        String thumbnailPath = videoDir + "/thumbnail.jpg";

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            Files.copy(thumbnailFile.getInputStream(), Paths.get(thumbnailPath), StandardCopyOption.REPLACE_EXISTING);
            log.info("Thumbnail subido por admin: {}", thumbnailPath);
        } else {
            generateThumbnail(videoFullPath, thumbnailPath);
        }

        return "/api/video/" + videoId + "/thumbnail";
    }

    private void deleteVideoDirectory(Video video) {
        if (video.getFilePath() == null) {
            return;
        }

        try {
            String basePath = systemConfigService.getConfigValue("video.storage.path");
            Path filePath = Paths.get(video.getFilePath());
            Path videoDir = Paths.get(basePath).resolve(filePath.getParent());

            deleteDirectoryRecursive(videoDir.toFile());
            log.info("Directorio eliminado: {}", videoDir);
        } catch (Exception e) {
            log.error("Error eliminando directorio del video id={}: {}", video.getId(), e.getMessage());
        }
    }

    private void deleteDirectoryRecursive(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectoryRecursive(file);
                    } else {
                        if (!file.delete()) {
                            log.warn("No se pudo eliminar archivo: {}", file.getAbsolutePath());
                        }
                    }
                }
            }
            if (!directory.delete()) {
                log.warn("No se pudo eliminar directorio: {}", directory.getAbsolutePath());
            }
        }
    }
}

