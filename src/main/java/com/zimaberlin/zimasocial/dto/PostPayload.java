package com.zimaberlin.zimasocial.dto;

import com.zimaberlin.zimasocial.entity.PostType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostPayload {
    private PostType type = PostType.any;
    @NotBlank(message = "Content is required")
    private String content;
    private String url;
}
