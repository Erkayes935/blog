package com.nolimit.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email (message = "Email tidak valid")
    @NotBlank (message = "Email tidak boleh kosong")
    private String email;

    @NotBlank (message = "Password tidak boleh kosong")
    private String password;

}