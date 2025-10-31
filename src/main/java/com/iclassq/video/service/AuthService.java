package com.iclassq.video.service;

import com.iclassq.video.dto.request.auth.LoginDTO;
import com.iclassq.video.dto.response.auth.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO login(LoginDTO dto);
}
