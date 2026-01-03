package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.api.post.PostCategory;
import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;
import com.zimaberlin.zimasocial.context.social.post.entity.Post;
import com.zimaberlin.zimasocial.context.social.post.repository.PostRepository;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.MediaType;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import com.zimaberlin.zimasocial.repository.PostJpaRepository;
import com.zimaberlin.zimasocial.repository.TodaysPostRepository;
import com.zimaberlin.zimasocial.repository.UserJpaRepository;
import com.zimaberlin.zimasocial.repository.UserRelationJpaRepository;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostDBRepository implements PostRepository {
    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userRepository;
    private final TodaysPostRepository todaysPostRepository;
    private final UserRelationJpaRepository userRelationJpaRepository;
    @Override
    public Optional<Post> findById(Long postId) {
        PostEntity post = postJpaRepository.findByIdAndIsVisibleTrue(postId).orElseThrow(PostNotFoundException::new);
        return Optional.ofNullable(post.rehydrate());
    }

    @Override
    public Page<Post> findAll(Pageable page, String slug, PostCategory category) {
        UserEntity currentUser = CurrentUser.getCurrentUserProfile();
        Specification<PostEntity> specification = Specification.where(null);
        if(slug != null){
            UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
            specification = specification.and(PostSpecification.authorId(user.getId()));
        }
        // Study Specification Pattern
        MediaType type = switch (category) {
                case any -> MediaType.any;
                case music -> MediaType.music;
                case movie -> MediaType.movie;
                case book -> MediaType.book;
                case tv ->  MediaType.tv;
                case followings -> null;
        };
        if(type != MediaType.any){
            specification = specification.and(PostSpecification.type(type));
        }
        List<UserRelationEntity> blockRelations = userRelationJpaRepository.findAllBlockRelations(currentUser.getId());
        List<UserRelationEntity> followRelations = userRelationJpaRepository.findByActorIdAndRelation(currentUser.getId(), Relation.followed);
        List<Long> followedAuthors = followRelations.stream().map(UserRelationEntity::getReceiverId).toList();

        specification = specification.and(PostSpecification.notBlocked(currentUser.getId(), blockRelations));
        specification = specification.and(PostSpecification.isVisible());
        specification = specification.and(PostSpecification.isAuthorPublicOrAuthorFollowed(currentUser.getId(), followedAuthors));
        Page<PostEntity> postEntityPage = postJpaRepository.findAll(specification, page);
        return postEntityPage.map(PostEntity::rehydrate);
    }

    @Override
    public Page<Post> findFollowingsPosts(Pageable page, AuthorId authorId) {
        return postJpaRepository.findFollowingsPosts(page, authorId.getValue()).map(PostEntity::rehydrate);
    }

    @Override
    public List<Post> findTodaysPosts() {
        List<TodaysPost> todaysPosts = todaysPostRepository.findTodaysPostByDate(LocalDate.now().minusDays(1));
        return todaysPosts.stream().map((e)-> e.getPost().rehydrate()).toList();
    }

    @Override
    public void delete(Post post) {
        PostEntity postEntity = postJpaRepository.findById(post.getPostId()).orElseThrow(PostNotFoundException::new);
        postEntity.markAsDeleted();
        postJpaRepository.save(postEntity);
    }

    @Override
    public Long nextSequence() {
        return postJpaRepository.getNextSequence();
    }

    @Override
    public List<Post> findAllByAuthorId(AuthorId authorId) {
        return postJpaRepository.findAllByUserId(authorId.getValue()).stream().map(PostEntity::rehydrate).toList();
    }

    @Override
    public List<Post> findAllInvisiblePostsByAuthorId(AuthorId authorId) {
        return postJpaRepository.
                findAllInvisiblePostsByUserId(authorId.getValue()).
                stream().map(PostEntity::rehydrate).toList();
    }

    @Override
    public List<Post> findAllByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end) {
        return postJpaRepository.findAllByCreatedAtBetween(start, end).stream().map(PostEntity::rehydrate).toList();
    }

    @Override
    @Transactional
    public Post save(Post post) {
        PostEntity postEntity = postJpaRepository.findById(post.getPostId()).orElse(new PostEntity());
        postEntity.merge(post);
        UserEntity user = userRepository.findById(post.getAuthorId().getValue()).orElseThrow(UserNotFoundException::new);
        postEntity.setUser(user);
        PostEntity savedPost = postJpaRepository.save(postEntity);
        return savedPost.rehydrate();
    }

    @Override
    public void makeInvisiblePostsOfAuthor(AuthorId authorId) {
        List<PostEntity> postEntityList = postJpaRepository.findAllByUserId(authorId.getValue());
        postEntityList.forEach(e->e.setIsVisible(false));
        postJpaRepository.saveAll(postEntityList);
    }

    @Override
    public void makePostsVisibleOfAuthor(AuthorId authorId) {
        List<PostEntity> postEntityList = postJpaRepository.findAllInvisiblePostsByUserId(authorId.getValue());
        postEntityList.forEach(e->e.setIsVisible(true));
        postJpaRepository.saveAll(postEntityList);
    }
}
