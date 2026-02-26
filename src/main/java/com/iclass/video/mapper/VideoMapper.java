package com.iclass.video.mapper;

import com.iclass.video.dto.request.video.CreateVideoDTO;
import com.iclass.video.dto.request.video.UpdateVideoDTO;
import com.iclass.video.dto.response.video.VideoResponseDTO;
import com.iclass.video.dto.response.video.VideoSimpleDTO;
import com.iclass.video.dto.response.video.VideoUploadResponseDTO;
import com.iclass.video.entity.Company;
import com.iclass.video.entity.Video;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoMapper {

    public Video toEntityForUpload(String name, Company company) {
        return Video.builder()
                .company(company)
                .name(name)
                .isActive(true)
                .build();
    }

    public void updateEntity(Video video, UpdateVideoDTO dto) {
        if (dto.getName() != null && !dto.getName().isBlank()) video.setName(dto.getName());
    }

    public VideoResponseDTO toResponseDTO(Video video) {
        return VideoResponseDTO.builder()
                .id(video.getId())
                .companyId(video.getCompany().getId())
                .companyName(video.getCompany().getName())
                .name(video.getName())
                .urlVideo(video.getUrlVideo())
                .thumbnail(video.getThumbnail())
                .duration(video.getDuration())
                .fileSize(video.getFileSize())
                .fileExtension(video.getFileExtension())
                .checksum(video.getChecksum())
                .isActive(video.getIsActive())
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .build();
    }

    public VideoUploadResponseDTO toUploadResponseDTO(Video video) {
        return VideoUploadResponseDTO.builder()
                .id(video.getId())
                .companyId(video.getCompany().getId())
                .companyName(video.getCompany().getName())
                .name(video.getName())
                .urlVideo(video.getUrlVideo())
                .thumbnail(video.getThumbnail())
                .duration(video.getDuration())
                .fileSize(video.getFileSize())
                .fileExtension(video.getFileExtension())
                .checksum(video.getChecksum())
                .isActive(video.getIsActive())
                .createdAt(video.getCreatedAt())
                .build();
    }

    public VideoSimpleDTO toSimpleDTO(Video video) {
        return VideoSimpleDTO.builder()
                .id(video.getId())
                .name(video.getName())
                .urlVideo(video.getUrlVideo())
                .thumbnail(video.getThumbnail())
                .duration(video.getDuration())
                .fileSize(video.getFileSize())
                .checksum(video.getChecksum())
                .build();
    }

    public List<VideoResponseDTO> toResponseDTOList(List<Video> videos) {
        return videos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<VideoSimpleDTO> toSimpleDTOList(List<Video> videos) {
        return videos.stream()
                .map(this::toSimpleDTO)
                .collect(Collectors.toList());
    }
}
