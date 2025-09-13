package com.zimaberlin.zimasocial.entity.media;
import com.zimaberlin.zimasocial.entity.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "MEDIA")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MediaJpa {
    @Id
    private UUID id;
    @OneToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;
    @Column(name = "post_id", insertable = false, updatable = false)
    Long postId;
    @Column(name = "comment_id")
    private Long commentId;
    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaType type;
    @Embedded
    private MusicMediaJpa song;
    @Embedded
    private MovieMediaJpa movie;
    @Embedded
    private BookMediaJpa book;
}
