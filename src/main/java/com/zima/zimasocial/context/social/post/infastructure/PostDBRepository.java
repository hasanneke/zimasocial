package com.zima.zimasocial.context.social.post.infastructure;

import com.zima.zimasocial.context.social.post.value.FeedFilter;
import com.zima.zimasocial.context.social.post.value.PostCategory;
import com.zima.zimasocial.context.social.post.repository.PostCustomRepository;
import com.zima.zimasocial.context.social.post.value.PostSortType;
import com.zima.zimasocial.context.social.post.api.views.PostDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PostDBRepository implements PostCustomRepository {
    private final EntityManager entityManager;
    private final String basePostSelectSQL = """
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
                     	EXISTS (SELECT 1 FROM user_relation       WHERE initiated_id = :user_id AND receiver_id = Users.id AND relation = 'followed') isFollowed,
                     	EXISTS (SELECT 1 FROM user_relation   	  WHERE initiated_id = Users.id AND receiver_id = :user_id AND relation = 'followed') isFollowingMe,
                     	EXISTS (SELECT 1 FROM follow_request 	  WHERE follower_id = :user_id AND followed_id = Users.id) isFollowRequestSent,
                     	EXISTS (SELECT 1 FROM follow_request      WHERE followed_id = :user_id AND follower_id = Users.id) isFollowRequestReceived,
                     	EXISTS (SELECT 1 FROM likes				  WHERE user_id = :user_id AND post_id = Post.id AND like_type = 'post') isLiked,
                     	EXISTS (SELECT 1 FROM report			  WHERE resource_id = Post.id AND reporter_id = :user_id AND resource_type = 'post') isReported
                    FROM post Post
    """;

    @Override
    public List<PostDTO> findFeed(FeedFilter feedFilter) {
        if(feedFilter.getCategory() == PostCategory.followings){
            return findFollowingsFeed(feedFilter);
        }else if(feedFilter.getOwnerAuthorId() != null){
            return findAuthorsFeed(feedFilter);
        }
        return findHomeFeed(feedFilter);
    }

    private List<PostDTO> findHomeFeed(FeedFilter filter) {
        StringBuilder baseSqlString = new StringBuilder(
                basePostSelectSQL
                        + """
                    INNER JOIN users Users ON Users.id = Post.user_id AND Users.is_deleted = false AND Users.is_private = false AND Users.is_disabled = false
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

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", filter.getReaderAuthorId().getValue());
        params.put("size", filter.getSize() != null ? filter.getSize() : 20);

        if(filter.getLastScore() != null && filter.getLastId() != null){
            baseSqlString.append(" AND Post.id < :last_id AND Post.score <= :last_score");
            params.put("last_id", filter.getLastId());
            params.put("last_score", filter.getLastScore());
        }

        if(filter.getCategory() != null && filter.getCategory().getType().isPresent()){
            baseSqlString.append(" AND Post.type = :type");
            params.put("type", filter.getCategory().getType().get().name());
        }

        if(filter.getSortType() == PostSortType.recent){
            baseSqlString.append(" ORDER BY Post.id DESC LIMIT :size");
        }else{
            baseSqlString.append(" ORDER BY Post.score DESC, Post.id DESC LIMIT :size");
        }

        Query query = entityManager.createNativeQuery(baseSqlString.toString(), "post_dto_mapping");
        params.forEach(query::setParameter);

        List<PostDTO> postDTOS = query.getResultList();
        return postDTOS;
    }

    private List<PostDTO> findFollowingsFeed(FeedFilter filter) {
        StringBuilder baseSQLString = new StringBuilder(
                basePostSelectSQL + """
                    INNER JOIN users Users ON Users.id = Post.user_id AND Users.is_deleted = false AND Users.is_private = false AND Users.is_disabled = false
                    WHERE
                    EXISTS (SELECT 1 FROM user_relation WHERE initiated_id = :user_id AND receiver_id = Users.id AND relation = 'followed')
                """);

        if(filter.getSortType() == PostSortType.recent){
            baseSQLString.append(" ORDER BY Post.id DESC LIMIT :size");
        }else{
            baseSQLString.append(" ORDER BY Post.score DESC, Post.id DESC LIMIT :size");
        }
        Query query = entityManager.createNativeQuery(baseSQLString.toString(), "post_dto_mapping");
        query.setParameter("user_id", filter.getReaderAuthorId().getValue());
        query.setParameter("size", filter.getSize() == null ? 20 : filter.getSize());

        if(filter.getLastScore() != null && filter.getLastId() != null){
            query.setParameter("last_id", filter.getLastId());
        }

        return query.getResultList();
    }

    private List<PostDTO> findAuthorsFeed(FeedFilter filter) {
        StringBuilder baseSQLStringBuilder = new StringBuilder(
                basePostSelectSQL + """
                    INNER JOIN users Users ON Users.id = Post.user_id
                    WHERE Post.user_id = :owner_author_id AND NOT EXISTS (
                        SELECT 1
                        FROM user_relation UserRelation
                        WHERE UserRelation.relation = 'blocked'
                          AND (
                                (UserRelation.initiated_id = :user_id AND UserRelation.receiver_id = :owner_author_id)
                             OR (UserRelation.initiated_id = :owner_author_id AND UserRelation.receiver_id = :user_id)
                          )
                    )
                """
        );
        if(filter.getCategory() != null && filter.getCategory().getType().isPresent()){
            baseSQLStringBuilder.append(" AND Post.type = :type");
        }
        baseSQLStringBuilder.append(" ORDER BY Post.id DESC OFFSET :offset LIMIT :size");
        Query query = entityManager.createNativeQuery(baseSQLStringBuilder.toString(), "post_dto_mapping");
        query.setParameter("user_id", filter.getReaderAuthorId().getValue());
        query.setParameter("owner_author_id", filter.getOwnerAuthorId().getValue());
        query.setParameter("size", filter.getSize() != null ? filter.getSize() : 20);
        query.setParameter("offset", filter.getOffset() != null ? filter.getOffset() : 0);
        if(filter.getCategory() != null && filter.getCategory().getType().isPresent()){
            query.setParameter("type", filter.getCategory().getType().get().name());
        }
        return query.getResultList();
    }
}
