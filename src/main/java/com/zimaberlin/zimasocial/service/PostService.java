package com.zimaberlin.zimasocial.service;
import com.zimaberlin.zimasocial.dto.CustomUserDetails;
import com.zimaberlin.zimasocial.dto.PostPayload;
import com.zimaberlin.zimasocial.entity.Like;
import com.zimaberlin.zimasocial.entity.Post;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.Profile;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.repository.LikeRepository;
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
    private PostRepository postRepository;
    private ProfileRepository profileRepository;
    private LikeRepository likeRepository;

    @Autowired
    public PostService(PostRepository postRepository, ProfileRepository profileRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.likeRepository = likeRepository;
    }

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
        PostType type = payload.getType();
        if(type == null){
            type = PostType.any;
        }
        Post post = Post.builder()
                .url(payload.getUrl())
                .type(type)
                .user(details.getProfile())
                .content(payload.getContent())
                .build();

        return postRepository.save(post);
    }

    public void deletePost(Long id){
         postRepository.deleteById(id);
    }

    public String togglePost(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        Profile profile = getCurrentUserProfile();
        Like like = likeRepository.findByUserAndPost(profile, post).orElse(null);

        if(like == null){
            Like createLike =Like.builder()
                    .post(post)
                    .user(profile)
                    .build();
             likeRepository.save(createLike);
             post.setLikeCount(post.getLikeCount() + 1);
             postRepository.save(post);
             return "LIKED";
        }else{
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
            likeRepository.delete(like);
            return "DELETED";
        }
    }

    private Profile getCurrentUserProfile() {
        CustomUserDetails principalId = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileRepository.findById(principalId.getProfile().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }
}
