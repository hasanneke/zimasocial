package com.zimaberlin.zimasocial.entity.media;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "MEDIA")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "post_id")
    private Long postId;
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
    private BookJpa book;
}
