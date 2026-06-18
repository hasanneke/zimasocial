package com.zima.zimasocial.context.social.post.value;

import com.zima.zimasocial.context.social.media.value.MediaType;
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
        this.text = text;
        this.type = type;
    }

    @Column(name = "content")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MediaType type;
}
