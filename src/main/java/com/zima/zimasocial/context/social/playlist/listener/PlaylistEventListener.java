package com.zima.zimasocial.context.social.playlist.listener;

import com.zima.zimasocial.context.account.event.AccountCreatedEvent;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.playlist.application.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaylistEventListener {
    private final PlayListService playListService;

//    @EventListener
    public void handleNewAccountCreated(AccountCreatedEvent accountCreatedEvent) {
        playListService.createDefaultPlaylistsForAuthor(new AuthorId(accountCreatedEvent.accountId()));
    }
}
