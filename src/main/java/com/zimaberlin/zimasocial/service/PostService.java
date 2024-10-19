package com.zimaberlin.zimasocial.service;
import com.zimaberlin.zimasocial.DTO.CustomUserDetails;
import com.zimaberlin.zimasocial.DTO.PostPayload;
import com.zimaberlin.zimasocial.entity.Like;
import com.zimaberlin.zimasocial.entity.Post;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.Profile;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.exception.UnauthorizedException;
import com.zimaberlin.zimasocial.repository.PostRepository;
import com.zimaberlin.zimasocial.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ProfileRepository profileRepository;

    public Page<Post> getPosts(int page, int size, PostType type) throws NoSuchMethodException {
        Pageable pageable = PageRequest.of(page, size);
        if(type == null){
            return postRepository.findAll(pageable);
        }else{
            return  postRepository.findByType(pageable, type);
        }
    }

    public Post createPost(@Validated PostPayload payload){
        CustomUserDetails details =(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = Post.builder()
                .url(payload.getUrl())
                .type(payload.getType())
                .user(details.getProfile())
                .content(payload.getContent())
                .build();
        postRepository.save(post);
        return post;
    }

    public void deletePost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if(!post.getUser().getId().equals(getCurrentUserProfile().getId())){
            throw new UnauthorizedException("You don't have permission to delete this post");
        }
         postRepository.deleteById(id);
    }

    public Post likePost(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        Profile profile = getCurrentUserProfile();
        Like like = Like.builder().post(post).user(profile).build();
        post.getLikes().add(like);
        Post createdPost = postRepository.save(post);
        return createdPost;
    }

    private Profile getCurrentUserProfile() {
        String principalId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileRepository.findById(Long.parseLong(principalId))
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }
}
