package com.wowls.userservice.dto;

import com.wowls.userservice.vo.ResponseOrder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String password;

    private String encryptedPassword;
    private String userId;
    private LocalDateTime createdAt;

    private List<ResponseOrder> orders;
}
