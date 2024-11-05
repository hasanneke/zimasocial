package com.zimaberlin.zimasocial.utility;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.service.posts.Payload.CommentPayload;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentEntity payloadToCommentEntity(CommentPayload payload);
}