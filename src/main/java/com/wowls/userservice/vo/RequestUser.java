package com.wowls.userservice.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {
    @NotNull(message = "can't be null")
    @Size(min = 2, message = "email can't be less than 2")
    private String email;
    @NotNull(message = "can't be null")
    @Size(min = 2, message = "email can't be less than 2")
    private String name;
    @NotNull(message = "can't be null")
    @Size(min = 2, message = "email can't be less than 2")
    private String password;
}
