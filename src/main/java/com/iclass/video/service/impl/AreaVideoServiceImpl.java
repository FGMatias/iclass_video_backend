package com.iclass.video.service.impl;

import com.iclass.video.dto.request.areavideo.CreateAreaVideoDTO;
import com.iclass.video.dto.request.areavideo.SyncPlaylistDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        if (areaVideoRepository.existsByVideo_IdAndArea_Id(dto.getVideoId(), dto.getAreaId())) {
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
    public void syncPlaylist(Integer areaId, SyncPlaylistDTO dto, Integer userId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new ResourceNotFoundException("Area", areaId));

        Integer areaCompanyId = area.getBranch().getCompany().getId();
        List<Video> videos = videoRepository.findAllById(dto.getVideoIds());

        if (videos.size() != dto.getVideoIds().size()) {
            throw new BadRequestException("Algunos videos no existen");
        }

        for (Video video : videos) {
            if (!video.getCompany().getId().equals(areaCompanyId)) {
                throw new BadRequestException("El video '" + video.getName() + "' no pertenece a la empresa del área");
            }
        }

        List<AreaVideo> currentPlaylist = areaVideoRepository.findByAreaWithVideos(areaId);

        Map<Integer, AreaVideo> currentVideosMap = currentPlaylist.stream()
                        .collect(Collectors.toMap(av -> av.getVideo().getId(), av -> av));

        List<AreaVideo> toSave = new ArrayList<>();
        List<Integer> newVideoIds = dto.getVideoIds();

        for (int i = 0; i < newVideoIds.size(); i++) {
            Integer videoId = newVideoIds.get(i);
            AreaVideo existingAreaVideo = currentVideosMap.get(videoId);

            if (existingAreaVideo != null) {
                existingAreaVideo.setOrden(i + 1);
                toSave.add(existingAreaVideo);
                currentVideosMap.remove(videoId);
            } else {
                Video video = videos.stream()
                        .filter(v -> v.getId().equals(videoId))
                        .findFirst()
                        .orElseThrow();

                AreaVideo areaVideo = new AreaVideo();
                areaVideo.setArea(area);
                areaVideo.setVideo(video);
                areaVideo.setOrden(i + 1);
                toSave.add(areaVideo);
            }
        }

        if (!currentVideosMap.isEmpty()) {
            areaVideoRepository.deleteAll(currentVideosMap.values());
        }

        areaVideoRepository.saveAll(toSave);

        log.info("Playlist sincronizada para área id={}: {} videos", areaId, dto.getVideoIds().size());
        eventPublisher.publishEvent(new PlayListChangedEvent(areaId, userId));
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
