package com.iclass.video.service;

import com.iclass.video.dto.request.branch.CreateBranchDTO;
import com.iclass.video.dto.request.branch.UpdateBranchDTO;
import com.iclass.video.dto.response.branch.BranchDetailDTO;
import com.iclass.video.dto.response.branch.BranchResponseDTO;

import java.util.List;

public interface BranchService {
    List<BranchResponseDTO> findAll();
    List<BranchResponseDTO> findByCompanyId(Integer companyId);
    BranchResponseDTO findById(Integer id);
    BranchResponseDTO create(CreateBranchDTO dto);
    BranchResponseDTO update(Integer id, UpdateBranchDTO dto);
    BranchDetailDTO detail(Integer id);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
}
