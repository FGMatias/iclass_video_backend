package com.iclassq.video.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSimpleDTO {
    private Integer id;
    private String username;
    private String name;
    private String email;
}
