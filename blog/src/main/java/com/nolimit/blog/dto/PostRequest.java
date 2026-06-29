package com.nolimit.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {

    @NotBlank (message = "Content tidak boleh kosong")
    private String content;

}