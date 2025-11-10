package com.iclassq.video.service.impl;

import com.iclassq.video.dto.request.area.CreateAreaDTO;
import com.iclassq.video.dto.request.area.UpdateAreaDTO;
import com.iclassq.video.dto.response.area.AreaResponseDTO;
import com.iclassq.video.entity.Area;
import com.iclassq.video.entity.Branch;
import com.iclassq.video.exception.DuplicateEntityException;
import com.iclassq.video.exception.ResourceNotFoundException;
import com.iclassq.video.mapper.AreaMapper;
import com.iclassq.video.repository.AreaRepository;
import com.iclassq.video.repository.BranchRepository;
import com.iclassq.video.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;
    private final BranchRepository branchRepository;
    private final AreaMapper areaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AreaResponseDTO> findAll() {
        List<Area> areas = areaRepository.findAll();
        return areaMapper.toResponseDTOList(areas);
    }

    @Override
    @Transactional(readOnly = true)
    public AreaResponseDTO findById(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        return areaMapper.toResponseDTO(area);
    }

    @Override
    @Transactional
    public AreaResponseDTO create(CreateAreaDTO dto) {
        if (areaRepository.existsByName(dto.getName())) {
            throw new DuplicateEntityException("Area", "nombre", dto.getName());
        }

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", dto.getBranchId()));

        Area area = areaMapper.toEntity(dto, branch);
        Area savedArea = areaRepository.save(area);

        return areaMapper.toResponseDTO(savedArea);
    }

    @Override
    @Transactional
    public AreaResponseDTO update(Integer id, UpdateAreaDTO dto) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        if (dto.getName() != null && !dto.getName().equals(area.getName())) {
            if (areaRepository.existsByName(dto.getName())) {
                throw new DuplicateEntityException("Area", "nombre", id);
            }
        }

        areaMapper.updateEntity(area, dto);

        Area updatedArea = areaRepository.save(area);

        return areaMapper.toResponseDTO(updatedArea);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!areaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Area", id);
        }

        areaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activate(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        area.setIsActive(true);
        areaRepository.save(area);
    }

    @Override
    @Transactional
    public void deactivate(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        area.setIsActive(false);
        areaRepository.save(area);
    }
}
