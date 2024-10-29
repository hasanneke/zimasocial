package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.domain.Comment;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.service.Posts.Payload.CommentPayload;
import org.checkerframework.checker.units.qual.C;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment entityToComment(CommentEntity entity);
    CommentEntity payloadToCommentEntity(CommentPayload payload);
}