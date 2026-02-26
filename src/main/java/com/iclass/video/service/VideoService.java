package com.iclass.video.service;

import com.iclass.video.dto.request.video.UpdateVideoDTO;
import com.iclass.video.dto.response.video.VideoResponseDTO;
import com.iclass.video.dto.response.video.VideoUploadResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    VideoUploadResponseDTO uploadVideo(MultipartFile file, Integer companyId, String name, MultipartFile thumbnail);
    ResponseEntity<Resource> streamVideo(Integer videoId);
    ResponseEntity<Resource> getThumbnail(Integer videoId);
    List<VideoResponseDTO> findAll();
    List<VideoResponseDTO> findByCompany(Integer companyId);
    VideoResponseDTO findById(Integer id);
    VideoResponseDTO update(Integer id, UpdateVideoDTO dto);
    void delete(Integer id, Integer userId);
    void activate(Integer id);
    void deactivate(Integer id);
}
