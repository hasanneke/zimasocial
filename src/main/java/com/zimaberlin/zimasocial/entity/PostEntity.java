package com.zimaberlin.zimasocial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.music.MusicMedia;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.entity.media.MediaJpa;
import com.zimaberlin.zimasocial.entity.media.MovieMediaJpa;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "post")
@SQLRestriction(value = "IS_DELETED IS false")
public class PostEntity {
    @Id
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "comment_count")
    private int commentCount = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @Column(name = "is_visible")
    private Boolean isVisible = true;

    @Column(name = "user_id",insertable = false, updatable = false)
    private Long userId;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CommentEntity> comments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private Set<TodaysPost> todaysPosts =  new HashSet<>();

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private MediaJpa media;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public void markAsDeleted() {
        this.isDeleted = true;
    }

    public void merge(Post post) {
        this.id = post.getPostId();
        this.content = post.getContent();
        this.type = post.getType();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.userId = post.getAuthorId().getValue();
        this.isVisible = post.getIsVisible();
    }

    public Post convertToPostDomain() {
        switch (this.getType()){
            case PostType.any -> {
                return new Post(this.getId(), this.getContent(), this.getLikeCount(), this.getCommentCount(), this.getCreatedAt(), this.getUpdatedAt(), new AuthorId(this.getUser().getId()));
            }
            case PostType.movie -> {
                MovieMedia movieDomain = null;
                if(this.getMedia() != null && this.getMedia().getMovie() != null){
                    final MovieMediaJpa movie = this.getMedia().getMovie();
                    movieDomain = MovieMedia
                            .builder()
                            .id(this.getMedia().getId())
                            .description(movie.getDescription())
                            .originalLanguage(movie.getOriginalLanguage())
                            .posterUrl(movie.getPosterUrl())
                            .summary(movie.getSummary())
                            .releaseDate(movie.getReleaseDate())
                            .voteAverage(movie.getVoteAverage())
                            .voteCount(movie.getVoteCount())
                            .imdbScore(movie.getImdbScore())
                            .name(movie.getName())
                            .movieGenres(movie.getMovieGenres())
                            .movieProvider(movie.getMovieProvider())
                            .build();
                    return new Post(this.getId(), this.getContent(), this.getLikeCount(), this.getCommentCount(), this.getCreatedAt(), this.getUpdatedAt(), new AuthorId(this.getUser().getId()), movieDomain);
                }
                return new Post(this.getId(), this.getContent(), this.getLikeCount(), this.getCommentCount(), this.getCreatedAt(), this.getUpdatedAt(), new AuthorId(this.getUser().getId()), movieDomain);
            }
            case PostType.book ->  {
                BookMedia bookMedia = null;
                if(this.getMedia() != null && this.getMedia().getBook() != null){
                    bookMedia = this.getMedia().getBook().toBookMedia(this.getMedia().getId());
                }
                return new Post(this.getId(), this.getContent(), this.getLikeCount(), this.getCommentCount(), this.getCreatedAt(), this.getUpdatedAt(), new AuthorId(this.getUser().getId()), bookMedia);
            }
            case PostType.music -> {
                MusicMedia musicMedia;
                if(this.getMedia() != null && this.getMedia().getSong() != null){
                    musicMedia = this.getMedia().getSong().toMusicMedia(this.getMedia().getId());
                    return new Post(this.getId(), this.getContent(), this.getLikeCount(), this.getCommentCount(), this.getCreatedAt(), this.getUpdatedAt(), new AuthorId(this.getUser().getId()), musicMedia);
                }
            }
            default -> {}
        }
        return null;
    }
}

