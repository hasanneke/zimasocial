package com.zima.zimasocial.context.social.playlist.entity;

import com.zima.zimasocial.context.account.exception.CharachterLengthExceededException;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.media.infastructure.MediaItem;
import com.zima.zimasocial.context.social.playlist.values.PlayListId;
import com.zima.zimasocial.context.social.playlist.values.PlayListItem;
import com.zima.zimasocial.context.social.post.value.MediaId;
import com.zima.zimasocial.entity.MediaType;
import com.zima.zimasocial.exception.BadRequestException;
import com.zima.zimasocial.exception.ConflictException;
import com.zima.zimasocial.exception.UnauthorizedException;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Playlist {
    private final PlayListId id;
    private String name;
    private AuthorId ownerId;
    private MediaType type;
    private Set<PlayListItem> items = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Playlist(PlayListId id) {
        this.id = id;
    }
    public Playlist(PlayListId id, String name, AuthorId authorId, MediaType type, Set<PlayListItem> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Assert.isTrue(type != MediaType.any, "type cannot be any");
        Assert.notNull(type, "type cannot be null");
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(type, "name cannot be null");
        Assert.notNull(id, "authorId cannot be null");
        verifyName(name);
        this.id = id;
        this.name = name;
        this.ownerId = authorId;
        this.type = type;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Playlist create(PlayListId id, String name, AuthorId authorId, MediaType type){
        Assert.isTrue(type != MediaType.any, "type cannot be any");
        Assert.notNull(type, "type cannot be null");
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(type, "name cannot be null");
        Assert.notNull(id, "authorId cannot be null");
        verifyName(name);
        Playlist playlist = new Playlist(id);
        playlist.name = name;
        playlist.ownerId = authorId;
        playlist.type = type;
        return playlist;
    }

    public static Playlist reconstitute(PlayListId id, String name, AuthorId ownerId, MediaType type, Set<PlayListItem> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Assert.isTrue(type != MediaType.any, "type cannot be any");
        Assert.notNull(type, "type cannot be null");
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(type, "name cannot be null");
        Assert.notNull(id, "authorId cannot be null");
        verifyName(name);
        Playlist playlist = new Playlist(id);
        playlist.name = name;
        playlist.ownerId = ownerId;
        playlist.type = type;
        playlist.items = items;
        playlist.createdAt = createdAt;
        playlist.updatedAt = updatedAt;
        return playlist;
    }

    public void addItem(MediaItem mediaItem, AuthorId modifier) {
        verifyOwnership(modifier);
        if(!mediaItem.getType().equals(type)){
            throw new BadRequestException("Selected media cannot be added to this playlist");
        }
        if(items.stream().anyMatch(e->e.getMediaId().equals(new MediaId(mediaItem.getId())))){
            throw new ConflictException("Listede mevcut tekrar eklenemez.");
        }
        if(items.size() >= 100){
            throw new ConflictException("Katalog 100 den fazla medya bulunduramaz");
        }
        items.add(new PlayListItem(new MediaId(mediaItem.getId())));
    }

    public void removeItem(MediaId mediaId, AuthorId authorId) {
        verifyOwnership(authorId);
        items.removeIf(e->e.getMediaId().equals(mediaId));
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
