package com.zima.zimasocial.context.social.infastructure.repository;

import com.zima.zimasocial.context.social.api.post.PostCategory;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.repository.FeedFilter;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
import com.zima.zimasocial.entity.MediaType;
import com.zima.zimasocial.entity.PostEntity;
import com.zima.zimasocial.entity.todayspost.TodaysPost;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.entity.userRelation.Relation;
import com.zima.zimasocial.entity.userRelation.UserRelationEntity;
import com.zima.zimasocial.repository.PostJpaRepository;
import com.zima.zimasocial.repository.TodaysPostRepository;
import com.zima.zimasocial.repository.UserJpaRepository;
import com.zima.zimasocial.repository.UserRelationJpaRepository;
import com.zima.zimasocial.service.posts.exception.PostNotFoundException;
import com.zima.zimasocial.service.users.exception.UserNotFoundException;
import com.zima.zimasocial.utility.CurrentUser;
import com.zima.zimasocial.views.post.PostDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
    private final EntityManager entityManager;
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

    @Override
    public List<PostDTO> findFeed(FeedFilter feedFilter) {
        StringBuilder baseSqlString = new StringBuilder("""
                    SELECT
                    	Post.id id,
                     	Post.content,
                     	Post.type,
                     	Post.like_count as likeCount,
                     	Post.comment_count as commentCount,
                     	Post.updated_at as updatedAt,
                     	Post.score,
                     	Post.media_id as mediaId,
                     	Post.created_at as createdAt,
                      	Users.id as userId,
                     	Users.slug,
                     	Users.name,
                     	Users.family_name as familyName,
                     	Users.avatar_file_name as avatarFileName,
                     	Users.bio,
                     	Users.follower_count as followerCount,
                     	Users.following_count as followingCount,
                     	EXISTS (SELECT 1 FROM user_relation       WHERE initiated_id = :user_id AND receiver_id = Users.id) isFollowed,
                     	EXISTS (SELECT 1 FROM user_relation   	  WHERE receiver_id = Users.id AND initiated_id = :user_id) isFollowingMe,
                     	EXISTS (SELECT 1 FROM follow_request 	  WHERE follower_id = :user_id AND followed_id = Users.id) isFollowRequestSent,
                     	EXISTS (SELECT 1 FROM follow_request      WHERE followed_id = :user_id AND follower_id = Users.id) isFollowRequestReceived,
                     	EXISTS (SELECT 1 FROM likes				  WHERE user_id = :user_id AND post_id = Post.id AND like_type = 'post') isLiked,
                     	EXISTS (SELECT 1 FROM report			  WHERE resource_id = Post.id AND reporter_id = Users.id AND resource_type = 'post') isReported
                    FROM post Post
                    INNER JOIN users Users ON Users.id = Post.user_id AND Users.is_deleted = false AND Users.is_private = false
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM user_relation UserRelation
                        WHERE UserRelation.relation = 'blocked'
                          AND (
                                (UserRelation.initiated_id = :user_id AND UserRelation.receiver_id = Users.id)
                             OR (UserRelation.initiated_id = Users.id AND UserRelation.receiver_id = :user_id)
                          )
                    )
                """);
        if(feedFilter.getLastCreatedAt() != null && feedFilter.getLastScore() != null && feedFilter.getLastId() != null){
            baseSqlString.append(" AND Post.score <= :last_score AND (Post.created_at, Post.id) < (:last_created_at, :last_id)");
        }
        if(feedFilter.getType() != null){
            baseSqlString.append(" AND Post.type = :type");
        }
        baseSqlString.append(" ORDER BY Post.score DESC, Post.created_at DESC, Post.id DESC LIMIT :size");
        Query query = entityManager.createNativeQuery(baseSqlString.toString(), "post_dto_mapping");

        query.setParameter("user_id", feedFilter.getUserId());
        query.setParameter("size", feedFilter.getSize() == null ? 20 : feedFilter.getSize());

        if(feedFilter.getLastCreatedAt() != null && feedFilter.getLastScore() != null && feedFilter.getLastId() != null){
            query.setParameter("last_score", feedFilter.getLastScore());
            query.setParameter("last_created_at", feedFilter.getLastCreatedAt());
            query.setParameter("last_id", feedFilter.getLastId());
        }

        if(feedFilter.getType() != null){
            query.setParameter("type", feedFilter.getType().name());
        }

        List<PostDTO> postDTOS = query.getResultList();
        return postDTOS;
    }
}
