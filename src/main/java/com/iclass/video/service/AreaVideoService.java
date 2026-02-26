package com.iclass.video.service;

import com.iclass.video.dto.request.areavideo.CreateAreaVideoDTO;
import com.iclass.video.dto.request.areavideo.UpdateAreaVideoDTO;
import com.iclass.video.dto.response.areavideo.AreaVideoResponseDTO;

import java.util.List;

public interface AreaVideoService {
    List<AreaVideoResponseDTO> findByArea(Integer areaId);
    AreaVideoResponseDTO create(CreateAreaVideoDTO dto, Integer userId);
    AreaVideoResponseDTO updateOrder(Integer id, UpdateAreaVideoDTO dto, Integer userId);
    void delete(Integer id, Integer userId);
}
