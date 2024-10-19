package com.zimaberlin.zimasocial.DTO;

import com.zimaberlin.zimasocial.entity.PostType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostPayload {
    private PostType type;
    @NotBlank(message = "Content is required")
    private String content;
    private String url;
}
