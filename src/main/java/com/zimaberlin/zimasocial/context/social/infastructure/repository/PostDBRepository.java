package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.context.social.infastructure.adapter.PostDBAdapter;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.repository.PostJpaRepository;
import com.zimaberlin.zimasocial.repository.TodaysPostRepository;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PostDBRepository implements PostRepository {
    private final PostJpaRepository postJpaRepository;
    private final PostDBAdapter postDBAdapter;
    private final UserRepository userRepository;
    private final TodaysPostRepository todaysPostRepository;
    @Autowired
    public PostDBRepository(PostJpaRepository postJpaRepository, PostDBAdapter postDBAdapter, UserRepository userRepository, TodaysPostRepository todaysPostRepository) {
        this.postJpaRepository = postJpaRepository;
        this.postDBAdapter = postDBAdapter;
        this.userRepository = userRepository;
        this.todaysPostRepository = todaysPostRepository;
    }
    @Override
    public Optional<Post> findById(Long postId) {
        PostEntity post = postJpaRepository.findById(postId).orElse(null);
        if(post == null){
            throw new PostNotFoundException();
        }
        return Optional.ofNullable(postDBAdapter.convertPostEntityToPost(post));
    }

    @Override
    public List<Post> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        List<PostEntity> postEntityPage = postJpaRepository.findAllByCreatedAtBetween(start, end);
        return postEntityPage.stream().map(postDBAdapter::convertPostEntityToPost).toList();
    }

    @Override
    public Page<Post> findAll(Pageable page, String slug, PostType type) {
        Specification<PostEntity> specification = Specification.where(null);
        if(slug != null){
            UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
            specification = specification.and(PostSpecification.authorId(user.getId()));
        }
        if(type == PostType.any || type == null){
            specification = specification.and(PostSpecification.type(PostType.any));
        }else{
            specification = specification.and(PostSpecification.type(type));
        }
        Page<PostEntity> postEntityPage = postJpaRepository.findAll(specification, page);
        return postEntityPage.map(postDBAdapter::convertPostEntityToPost);
    }

    @Override
    public List<Post> findTodaysPosts() {
        List<TodaysPost> todaysPosts = todaysPostRepository.findTodaysPostByDate(LocalDate.now().minusDays(1));
        return todaysPosts.stream().map((e)-> postDBAdapter.convertPostEntityToPost(e.getPost())).toList();
    }

    @Override
    public void delete(Post post) {
        PostEntity postEntity = postJpaRepository.findById(post.getPostId()).orElseThrow(PostNotFoundException::new);
        postEntity.markAsDeleted();
        postJpaRepository.save(postEntity);
    }

    @Override
    public Post save(Post post) {
        PostEntity postEntity;
        if(post.getPostId() != null){
            postEntity = postJpaRepository.findById(post.getPostId()).orElseThrow(PostNotFoundException::new);
            postEntity.merge(post);
        }else{
            postEntity = new PostEntity();
            postEntity.merge(post);
            UserEntity user = userRepository.findById(post.getAuthorId()).orElseThrow(UserNotFoundException::new);
            postEntity.setUser(user);
        }
        postEntity = postJpaRepository.save(postEntity);
        return postDBAdapter.convertPostEntityToPost(postEntity);

    }
}
