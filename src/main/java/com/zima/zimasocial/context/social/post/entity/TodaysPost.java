package com.zima.zimasocial.context.social.post.entity;


import com.zima.zimasocial.context.social.author.value.AuthorId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "todays_post")
@SQLRestriction(value = "IS_DELETED IS FALSE")
public class TodaysPost {
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "todays_date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "author_id", nullable = false, updatable = false)
    )
    private AuthorId authorId;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void setAsDeleted() {
        this.isDeleted = true;
    }
}

