package com.iclass.video.dto.response.branch;

import com.iclass.video.dto.response.area.AreaResponseDTO;
import com.iclass.video.dto.response.device.DeviceInfo;
import com.iclass.video.dto.response.user.UserResponseDTO;
import com.iclass.video.dto.response.user.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDetailDTO {
    private Integer id;
    private Integer companyId;
    private String companyName;
    private String name;
    private String direction;
    private String phone;
    private Boolean isActive;
    private List<UserResponseDTO> administrators;
    private List<AreaResponseDTO> areas;
    private List<DeviceInfo> devices;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
