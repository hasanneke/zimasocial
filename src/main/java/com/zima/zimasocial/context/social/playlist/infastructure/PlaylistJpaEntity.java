package com.zima.zimasocial.context.social.playlist.infastructure;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.playlist.entity.Playlist;
import com.zima.zimasocial.context.social.playlist.values.PlayListId;
import com.zima.zimasocial.context.social.playlist.values.PlayListItem;
import com.zima.zimasocial.context.social.post.value.MediaId;
import com.zima.zimasocial.entity.MediaType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Table(name = "playlist")
@Entity
@Getter
@Setter
public class PlaylistJpaEntity {
    @Id
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ElementCollection
    @CollectionTable(
            name = "playlist_item",
            joinColumns = @JoinColumn(name = "playlist_id")
    )
    private Set<PlaylistItemJpaEntity> items = new HashSet<>();

    public PlaylistJpaEntity() {
    }
    public PlaylistJpaEntity(Playlist playlist) {
        this.id = playlist.getId().value();
        this.name = playlist.getName();
        this.userId = playlist.getOwnerId().getValue();
        this.type = playlist.getType();
        this.items =  playlist.getItems().stream().map(PlaylistItemJpaEntity::new).collect(Collectors.toSet());
    }

    public Playlist rehydrate() {
        return Playlist.reconstitute(new PlayListId(id), name, new AuthorId(userId), type, items.stream().map(e->new PlayListItem(new MediaId(e.getMediaItemId()))).collect(Collectors.toSet()), createdAt, updatedAt);
    }

    public void merge(Playlist playlist){
        this.name = playlist.getName();
        this.items = playlist.getItems().stream().map(PlaylistItemJpaEntity::new).collect(Collectors.toSet());
    }
}
