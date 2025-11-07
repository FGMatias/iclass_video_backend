package com.iclassq.video.service;

import com.iclassq.video.dto.request.company.CreateCompanyDTO;
import com.iclassq.video.dto.request.company.UpdateCompanyDTO;
import com.iclassq.video.dto.response.company.CompanyResponseDTO;

import java.util.List;

public interface CompanyService {
    List<CompanyResponseDTO> findAll();
    CompanyResponseDTO findById(Integer id);
    CompanyResponseDTO create(CreateCompanyDTO dto);
    CompanyResponseDTO update(Integer id, UpdateCompanyDTO dto);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
}
