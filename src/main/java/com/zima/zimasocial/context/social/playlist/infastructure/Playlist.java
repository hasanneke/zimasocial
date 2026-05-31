package com.zima.zimasocial.context.social.playlist.infastructure;

import com.zima.zimasocial.context.account.exception.CharachterLengthExceededException;
import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.playlist.values.PlayListId;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.media.value.MediaId;
import com.zima.zimasocial.entity.MediaType;
import com.zima.zimasocial.exception.BadRequestException;
import com.zima.zimasocial.exception.ConflictException;
import com.zima.zimasocial.exception.UnauthorizedException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "playlist")
@Entity
@Getter
@Setter
public class Playlist {
    @Id
    private UUID id;
    @Column(name = "name")
    private String name;
    @Embedded
    private AuthorId ownerId;
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
    private Set<PlaylistItem> items = new HashSet<>();

    protected Playlist() {
    }


    public static Playlist create(PlayListId id, String name, AuthorId authorId, MediaType type){
        Assert.isTrue(type != MediaType.any, "type cannot be any");
        Assert.notNull(type, "type cannot be null");
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(type, "name cannot be null");
        Assert.notNull(id, "authorId cannot be null");
        verifyName(name);
        Playlist playlist = new Playlist();
        playlist.id = id.value();
        playlist.name = name;
        playlist.ownerId = authorId;
        playlist.type = type;
        return playlist;
    }

    public void addItem(Media media, AuthorId modifier) {
        verifyOwnership(modifier);
        if(!media.getType().equals(type)){
            throw new BadRequestException("Selected media cannot be added to this playlist");
        }
        if(items.stream().anyMatch(e->e.getMediaItemId().equals(media.getId()))){
            throw new ConflictException("Listede mevcut tekrar eklenemez.");
        }
        if(items.size() >= 100){
            throw new ConflictException("Katalog 100 den fazla medya bulunduramaz");
        }
        items.add(new PlaylistItem(new MediaId(media.getId().getValue())));
    }

    public void removeItem(MediaId mediaId, AuthorId authorId) {
        verifyOwnership(authorId);
        items.removeIf(e->e.getMediaItemId().equals(mediaId));
    }
    public void updateName(String name, AuthorId modifier) {
        verifyOwnership(modifier);
        verifyName(name);
        this.name = name;
    }

    private void verifyOwnership(AuthorId modifier) {
        if(!modifier.equals(ownerId)){
            throw new UnauthorizedException("Author is not the owner of the list");
        }
    }

    static void verifyName(String name) {
        if(name.length() > 24){
            throw new CharachterLengthExceededException(24);
        }
    }
}
