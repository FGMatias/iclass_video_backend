package com.iclass.video.service;

import com.iclass.video.dto.request.area.CreateAreaDTO;
import com.iclass.video.dto.request.area.UpdateAreaDTO;
import com.iclass.video.dto.response.area.AreaDetailDTO;
import com.iclass.video.dto.response.area.AreaResponseDTO;

import java.util.List;

public interface AreaService {
    List<AreaResponseDTO> findAll();
    List<AreaResponseDTO> findByBranchId(Integer branchId);
    AreaResponseDTO findById(Integer id);
    AreaDetailDTO detail(Integer id);
    AreaResponseDTO create(CreateAreaDTO dto);
    AreaResponseDTO update(Integer id, UpdateAreaDTO dto);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
}
