package com.wowls.userservice.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ResponseUser {
    private String userId;
    private String email;
    private String name;
}
