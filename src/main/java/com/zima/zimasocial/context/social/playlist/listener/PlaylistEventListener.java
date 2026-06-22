package com.zima.zimasocial.context.social.playlist.listener;

import com.zima.zimasocial.context.account.event.AccountCreatedEvent;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.playlist.application.PlaylistApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PlaylistEventListener {
    private final PlaylistApplicationService playListService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNewAccountCreated(AccountCreatedEvent accountCreatedEvent) {
        playListService.createDefaultPlaylistsForAuthor(new AuthorId(accountCreatedEvent.accountId()));
    }
}
