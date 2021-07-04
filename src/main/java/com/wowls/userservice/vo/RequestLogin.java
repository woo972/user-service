package com.wowls.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {
    @NotNull(message = "email can't be null")
    @Size(min =2, message = "more than 2 plz")
    @Email
    private String email;

    @NotNull(message = "password can't be null")
    @Size(min =2, message = "more than 2 plz")
    private String password;
    
}
