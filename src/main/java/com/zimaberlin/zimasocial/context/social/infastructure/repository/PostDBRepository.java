package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.context.social.infastructure.adapter.PostPostEntityAdapter;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.repository.PostJpaRepository;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PostDBRepository implements PostRepository {
    private final PostJpaRepository postJpaRepository;
    private final PostPostEntityAdapter postPostEntityAdapter;
    private final UserRepository userRepository;
    @Autowired
    public PostDBRepository(PostJpaRepository postJpaRepository, PostPostEntityAdapter postPostEntityAdapter, UserRepository userRepository) {
        this.postJpaRepository = postJpaRepository;
        this.postPostEntityAdapter = postPostEntityAdapter;
        this.userRepository = userRepository;
    }
    @Override
    public Optional<Post> findById(Long postId) {
        PostEntity post = postJpaRepository.findById(postId).orElse(null);
        if(post == null){
            throw new PostNotFoundException();
        }
        return Optional.ofNullable(postPostEntityAdapter.convertPostEntityToPost(post));
    }

    @Override
    public Page<Post> findByType(Pageable page, PostType type) {
        Page<PostEntity> postEntityPage = postJpaRepository.findByType(page, type);
        return postEntityPage.map(postPostEntityAdapter::convertPostEntityToPost);
    }

    @Override
    public Page<Post> findByUserOrderByCreatedAt(Pageable page, UserEntity user) {
        Page<PostEntity> postEntityPage = postJpaRepository.findByUserOrderByCreatedAt(page, user);
        return postEntityPage.map(postPostEntityAdapter::convertPostEntityToPost);
    }

    @Override
    public Page<Post> findByUserAndTypeOrderByCreatedAt(Pageable page, UserEntity user, PostType type) {
        Page<PostEntity> postEntityPage = postJpaRepository.findByUserAndTypeOrderByCreatedAt(page, user, type);
        return postEntityPage.map(postPostEntityAdapter::convertPostEntityToPost);
    }

    @Override
    public List<Post> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        List<PostEntity> postEntityPage = postJpaRepository.findAllByCreatedAtBetween(start, end);
        return postEntityPage.stream().map(postPostEntityAdapter::convertPostEntityToPost).toList();
    }
    @Override
    public void delete(Post post) {

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
        return postPostEntityAdapter.convertPostEntityToPost(postEntity);

    }
}
