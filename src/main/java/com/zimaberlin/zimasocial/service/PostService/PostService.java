package com.zimaberlin.zimasocial.service.PostService;
import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.dto.CustomUserDetails;
import com.zimaberlin.zimasocial.dto.PostPayload;
import com.zimaberlin.zimasocial.entity.*;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.repository.CommentRepository;
import com.zimaberlin.zimasocial.repository.LikeRepository;
import com.zimaberlin.zimasocial.repository.PostRepository;
import com.zimaberlin.zimasocial.repository.ProfileRepository;
import com.zimaberlin.zimasocial.service.PostService.Payload.CommentPayload;
import com.zimaberlin.zimasocial.utility.PostMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
public class PostService {
    private PostRepository postRepository;
    private LikeRepository likeRepository;
    private CommentRepository commentRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public Page<Post> getPosts(int page, int size, PostType type) throws NoSuchMethodException {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postsPage;
        if(type == null){
          postsPage = postRepository.findAll(pageable);
        }else{
            postsPage = postRepository.findByType(pageable, type);
        }

        List<Post> posts = postsPage.getContent().stream().map(PostMapper::PostEntityToPost).toList();
        Object authenticationPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(authenticationPrincipal != "anonymousUser"){
            ProfileEntity profile =  ((CustomUserDetails) authenticationPrincipal).getProfile();
            for (int i = 0; i < posts.size(); i++) {
                LikeEntity like = likeRepository.findByUserAndPost(profile, postsPage.getContent().get(i)).orElse(null);
                if(like != null){
                    posts.get(i).setLiked(true);
                }
            }
        }
        return new PageImpl<>(posts, pageable, postsPage.getTotalElements());
    }

    public PostEntity createPost(@Validated PostPayload payload){
        CustomUserDetails details =(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostType type = payload.getType();
        if(type == null){
            type = PostType.any;
        }

        PostEntity postEntity = PostEntity.builder()
                .url(payload.getUrl())
                .type(type)
                .user(details.getProfile())
                .content(payload.getContent())
                .build();

        return postRepository.save(postEntity);
    }

    public void deletePost(Long id){
         postRepository.deleteById(id);
    }

    public String togglePost(Long id){
        PostEntity postEntity = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        ProfileEntity profile = getCurrentUserProfile();
        LikeEntity like = likeRepository.findByUserAndPost(profile, postEntity).orElse(null);
        if(like == null){
            LikeEntity createLike = LikeEntity.builder()
                    .post(postEntity)
                    .user(profile)
                    .build();
            postEntity.setLikeCount(postEntity.getLikeCount() + 1);
            postRepository.save(postEntity);
            likeRepository.save(createLike);
             return "LIKED";
        } else {
            postEntity.setLikeCount(postEntity.getLikeCount() - 1);
            postRepository.save(postEntity);
            likeRepository.deleteById(like.getId());
            return "UNLIKED";
        }
    }

    private ProfileEntity getCurrentUserProfile() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getProfile();
    }
}
