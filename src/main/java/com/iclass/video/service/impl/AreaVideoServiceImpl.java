package com.iclass.video.service.impl;

import com.iclass.video.dto.request.areavideo.CreateAreaVideoDTO;
import com.iclass.video.dto.request.areavideo.UpdateAreaVideoDTO;
import com.iclass.video.dto.response.areavideo.AreaVideoResponseDTO;
import com.iclass.video.entity.Area;
import com.iclass.video.entity.AreaVideo;
import com.iclass.video.entity.Video;
import com.iclass.video.event.PlayListChangedEvent;
import com.iclass.video.exception.BadRequestException;
import com.iclass.video.exception.DuplicateEntityException;
import com.iclass.video.exception.ResourceNotFoundException;
import com.iclass.video.mapper.AreaVideoMapper;
import com.iclass.video.repository.AreaRepository;
import com.iclass.video.repository.AreaVideoRepository;
import com.iclass.video.repository.VideoRepository;
import com.iclass.video.service.AreaVideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AreaVideoServiceImpl implements AreaVideoService {

    private final AreaVideoRepository areaVideoRepository;
    private final VideoRepository videoRepository;
    private final AreaRepository areaRepository;
    private final AreaVideoMapper areaVideoMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    public List<AreaVideoResponseDTO> findByArea(Integer areaId) {
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Área", areaId);
        }

        List<AreaVideo> areaVideos = areaVideoRepository.findByAreaWithVideos(areaId);
        return areaVideoMapper.toResponseDTOList(areaVideos);
    }

    @Override
    @Transactional
    public AreaVideoResponseDTO create(CreateAreaVideoDTO dto, Integer userId) {
        Video video = videoRepository.findById(dto.getVideoId())
                .orElseThrow(() -> new ResourceNotFoundException("Video", dto.getVideoId()));

        Area area = areaRepository.findById(dto.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área", dto.getAreaId()));

        Integer videoCompanyId = video.getCompany().getId();
        Integer areaCompanyId = area.getBranch().getCompany().getId();

        if (!videoCompanyId.equals(areaCompanyId)) {
            throw new BadRequestException("El video y el área deben pertenecer a la misma empresa");
        }

        if (!areaVideoRepository.existsByVideo_IdAndArea_Id(dto.getVideoId(), dto.getAreaId())) {
            throw new DuplicateEntityException("El video ya está asignado a esta área");
        }

        AreaVideo areaVideo = areaVideoMapper.toEntity(video, area, dto.getOrden());
        AreaVideo savedAreaVideo = areaVideoRepository.save(areaVideo);

        log.info("Video id={} asignado a área id={} con orden={}", dto.getVideoId(), dto.getAreaId(), dto.getOrden());

        eventPublisher.publishEvent(new PlayListChangedEvent(dto.getAreaId(), userId));
        log.info("PlaylistChangedEvent publicado para área {}", dto.getAreaId());

        return areaVideoMapper.toResponseDTO(savedAreaVideo);
    }

    @Override
    @Transactional
    public AreaVideoResponseDTO updateOrder(Integer id, UpdateAreaVideoDTO dto, Integer userId) {
        AreaVideo areaVideo = areaVideoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AreaVideo", id));

        areaVideo.setOrden(dto.getOrden());
        areaVideoRepository.save(areaVideo);

        log.info("AreaVideo id={} orden actualizado a {}", id, dto.getOrden());

        Integer areaId = areaVideo.getArea().getId();
        eventPublisher.publishEvent(new PlayListChangedEvent(areaId, userId));
        log.info("PlaylistChangedEvent publicado para área {}", areaId);

        return areaVideoMapper.toResponseDTO(areaVideo);
    }

    @Override
    @Transactional
    public void delete(Integer id, Integer userId) {
        AreaVideo areaVideo = areaVideoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AreaVideo", id));

        Integer areaId = areaVideo.getArea().getId();
        Integer videoId = areaVideo.getVideo().getId();

        areaVideoRepository.delete(areaVideo);
        log.info("AreaVideo eliminado: id={}, videoId={}, areaId={}", id, videoId, areaId);

        eventPublisher.publishEvent(new PlayListChangedEvent(areaId, userId));
        log.info("PlaylistChangedEvent publicado para área {}", areaId);
    }
}
