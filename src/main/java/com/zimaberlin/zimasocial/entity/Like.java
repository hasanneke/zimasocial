package com.zimaberlin.zimasocial.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Profile user;

    @JoinColumn(name = "post_id")
    @ManyToOne
    @JsonBackReference
    private Post post;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
