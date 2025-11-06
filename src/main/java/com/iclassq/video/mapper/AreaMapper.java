package com.iclassq.video.mapper;

import com.iclassq.video.dto.request.area.CreateAreaDTO;
import com.iclassq.video.dto.request.area.UpdateAreaDTO;
import com.iclassq.video.dto.response.area.AreaDetailDTO;
import com.iclassq.video.dto.response.area.AreaResponseDTO;
import com.iclassq.video.dto.response.video.VideoSimpleDTO;
import com.iclassq.video.entity.Area;
import com.iclassq.video.entity.AreaVideo;
import com.iclassq.video.entity.Branch;
import com.iclassq.video.repository.AreaVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AreaMapper {
    private final AreaVideoRepository areaVideoRepository;

    public Area toEntity(CreateAreaDTO dto, Branch branch) {
        return Area.builder()
                .branch(branch)
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(true)
                .build();
    }

    public void updateEntity(Area area, UpdateAreaDTO dto) {
        if (dto.getName() != null) area.setName(dto.getName());
        if (dto.getDescription() != null) area.setDescription(dto.getDescription());
        if (dto.getIsActive() != null) area.setIsActive(dto.getIsActive());
    }

    public AreaResponseDTO toResponseDTO(Area area) {
        return AreaResponseDTO.builder()
                .id(area.getId())
                .branchId(area.getBranch().getId())
                .branchName(area.getBranch().getName())
                .companyId(area.getBranch().getCompany().getId())
                .companyName(area.getBranch().getCompany().getName())
                .name(area.getName())
                .description(area.getDescription())
                .isActive(area.getIsActive())
                .createdAt(area.getCreatedAt())
                .updatedAt(area.getUpdatedAt())
                .build();
    }

    public AreaDetailDTO toDetailDTO(Area area) {
        List<AreaVideo> areaVideos = areaVideoRepository.findByAreaWithVideos(area.getId());

        return AreaDetailDTO.builder()
                .id(area.getId())
                .name(area.getName())
                .description(area.getDescription())
                .isActive(area.getIsActive())
                .branch(AreaDetailDTO.BranchInfo.builder()
                        .id(area.getBranch().getId())
                        .name(area.getBranch().getName())
                        .companyName(area.getBranch().getCompany().getName())
                        .build())
                .videos(areaVideos.stream()
                        .sorted(Comparator.comparing(AreaVideo::getOrden))
                        .map(av -> VideoSimpleDTO.builder()
                                .id(av.getVideo().getId())
                                .name(av.getVideo().getName())
                                .urlVideo(av.getVideo().getUrlVideo())
                                .thumbnail(av.getVideo().getThumbnail())
                                .duration(av.getVideo().getDuration())
                                .orden(av.getOrden())
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(area.getCreatedAt())
                .build();
    }

    public List<AreaResponseDTO> toResponseDTOList(List<Area> areas) {
        return areas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
