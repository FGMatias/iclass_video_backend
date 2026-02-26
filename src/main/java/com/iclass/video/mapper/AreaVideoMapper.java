package com.iclass.video.mapper;

import com.iclass.video.dto.response.areavideo.AreaVideoResponseDTO;
import com.iclass.video.entity.Area;
import com.iclass.video.entity.AreaVideo;
import com.iclass.video.entity.Video;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AreaVideoMapper {
    public AreaVideo toEntity(Video video, Area area, Integer orden) {
        return AreaVideo.builder()
                .video(video)
                .area(area)
                .orden(orden)
                .build();
    }

    public AreaVideoResponseDTO toResponseDTO(AreaVideo areaVideo) {
        return AreaVideoResponseDTO.builder()
                .id(areaVideo.getId())
                .videoId(areaVideo.getVideo().getId())
                .videoName(areaVideo.getVideo().getName())
                .videoThumbnail(areaVideo.getVideo().getThumbnail())
                .videoDuration(areaVideo.getVideo().getDuration())
                .areaId(areaVideo.getArea().getId())
                .areaName(areaVideo.getArea().getName())
                .orden(areaVideo.getOrden())
                .createdAt(areaVideo.getCreatedAt())
                .build();
    }

    public List<AreaVideoResponseDTO> toResponseDTOList(List<AreaVideo> areaVideos) {
        return areaVideos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
