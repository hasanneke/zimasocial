package com.zima.zimasocial.context.social.playlist.listener;

import com.zima.zimasocial.context.account.event.AccountCreatedEvent;
import com.zima.zimasocial.context.communication.application.NotificationManager;
import com.zima.zimasocial.context.communication.domain.entity.AuthorAddedYourMediaToTheirListNotification;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.playlist.application.PlaylistApplicationService;
import com.zima.zimasocial.context.social.playlist.event.PlaylistItemAdded;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.exception.PostNotFoundException;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class PlaylistEventListener {
    private final PlaylistApplicationService playListService;
    private final NotificationManager notificationManager;
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNewAccountCreated(AccountCreatedEvent accountCreatedEvent) {
        playListService.createDefaultPlaylistsForAuthor(new AuthorId(accountCreatedEvent.accountId()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePlaylistItemAdded(PlaylistItemAdded playlistItemAdded) {
        Post referencedPost = postRepository.findById(playlistItemAdded.postIdReferencedFrom()).orElseThrow(PostNotFoundException::new);
        Author adder = authorRepository.getAuthenticatedAuthor();
        AuthorAddedYourMediaToTheirListNotification authorAddedYourMediaToTheirListNotification = AuthorAddedYourMediaToTheirListNotification
                .builder()
                .actorId(new RecipientId(adder.getId().getValue()))
                .recipientId(new RecipientId(referencedPost.getAuthorId().getValue()))
                .postIdReferencedFrom(playlistItemAdded.postIdReferencedFrom())
                .createdAt(OffsetDateTime.now())
                .build();
        notificationManager.throttled().sendNotification(authorAddedYourMediaToTheirListNotification);
    }
}
