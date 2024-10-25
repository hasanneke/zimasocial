package com.zimaberlin.zimasocial.service.CommentService;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.repository.CommentRepository;
import com.zimaberlin.zimasocial.repository.PostRepository;
import com.zimaberlin.zimasocial.service.PostService.Payload.CommentPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }
}
