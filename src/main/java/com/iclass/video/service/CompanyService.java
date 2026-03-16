package com.iclass.video.service;

import com.iclass.video.dto.request.company.CreateCompanyDTO;
import com.iclass.video.dto.request.company.UpdateCompanyDTO;
import com.iclass.video.dto.response.company.CompanyDetailDTO;
import com.iclass.video.dto.response.company.CompanyResponseDTO;

import java.util.List;

public interface CompanyService {
    List<CompanyResponseDTO> findAll();
    CompanyResponseDTO findById(Integer id);
    CompanyResponseDTO create(CreateCompanyDTO dto);
    CompanyResponseDTO update(Integer id, UpdateCompanyDTO dto);
    CompanyDetailDTO detail(Integer id);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
}
