package com.zima.zimasocial.context.social2.domain.entity;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.domain.value.CommentId;
import com.zima.zimasocial.context.social2.domain.value.PostId;
import com.zima.zimasocial.entity.LikeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "likes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private AuthorId authorId;

    @Embedded
    private PostId postId;

    @Embedded
    private CommentId commentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_type")
    private LikeType type;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like that = (Like) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
