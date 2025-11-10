package com.iclassq.video.service;

import com.iclassq.video.dto.request.area.CreateAreaDTO;
import com.iclassq.video.dto.request.area.UpdateAreaDTO;
import com.iclassq.video.dto.response.area.AreaResponseDTO;

import java.util.List;

public interface AreaService {
    List<AreaResponseDTO> findAll();
    AreaResponseDTO findById(Integer id);
    AreaResponseDTO create(CreateAreaDTO dto);
    AreaResponseDTO update(Integer id, UpdateAreaDTO dto);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
}
