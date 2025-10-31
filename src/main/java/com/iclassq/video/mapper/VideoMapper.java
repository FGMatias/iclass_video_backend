package com.iclassq.video.mapper;

import com.iclassq.video.dto.request.video.CreateVideoDTO;
import com.iclassq.video.dto.request.video.UpdateVideoDTO;
import com.iclassq.video.dto.response.video.VideoResponseDTO;
import com.iclassq.video.dto.response.video.VideoSimpleDTO;
import com.iclassq.video.entity.Company;
import com.iclassq.video.entity.Video;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoMapper {

    public Video toEntity(CreateVideoDTO dto, Company company) {
        return Video.builder()
                .company(company)
                .name(dto.getName())
                .urlVideo(dto.getUrlVideo())
                .duration(dto.getDuration())
                .thumbnail(dto.getThumbnail())
                .isActive(true)
                .build();
    }

    public void updateEntity(Video video, UpdateVideoDTO dto) {
        if (dto.getName() != null) video.setName(dto.getName());
        if (dto.getUrlVideo() != null) video.setUrlVideo(dto.getUrlVideo());
        if (dto.getDuration() != null) video.setDuration(dto.getDuration());
        if (dto.getThumbnail() != null) video.setThumbnail(dto.getThumbnail());
        if (dto.getIsActive() != null) video.setIsActive(dto.getIsActive());
    }

    public VideoResponseDTO toResponseDTO(Video video) {
        return VideoResponseDTO.builder()
                .id(video.getId())
                .companyId(video.getCompany().getId())
                .companyName(video.getCompany().getName())
                .name(video.getName())
                .urlVideo(video.getUrlVideo())
                .duration(video.getDuration())
                .thumbnail(video.getThumbnail())
                .isActive(video.getIsActive())
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .build();
    }

    public VideoSimpleDTO toSimpleDTO(Video video) {
        return VideoSimpleDTO.builder()
                .id(video.getId())
                .name(video.getName())
                .urlVideo(video.getUrlVideo())
                .thumbnail(video.getThumbnail())
                .duration(video.getDuration())
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
