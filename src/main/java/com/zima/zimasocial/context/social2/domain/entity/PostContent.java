package com.zima.zimasocial.context.social2.domain.entity;

import com.zima.zimasocial.entity.MediaType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.springframework.util.Assert;

@Embeddable
@Getter
public class PostContent {

    protected PostContent() {}

    public PostContent(String text, MediaType type) {
        Assert.notNull(type, "Type cannot be null");
        if(type == MediaType.any){
            Assert.notNull(text, "Text cannot be null");
        }
    }

    @Column(name = "content")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MediaType type;
}
