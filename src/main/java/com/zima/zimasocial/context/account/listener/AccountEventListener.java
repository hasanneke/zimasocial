package com.zima.zimasocial.context.account.listener;

import com.zima.zimasocial.context.account.event.AccountActivatedEvent;
import com.zima.zimasocial.context.account.event.AccountDeletedEvent;
import com.zima.zimasocial.context.account.event.AccountDisabledEvent;
import com.zima.zimasocial.context.social.post.application.PostApplicationService;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountEventListener {
    private final PostRepository postRepository;
    private final PostApplicationService postService;

    @EventListener
    public void handleAccountDisabledEvent(AccountDisabledEvent accountDisabledEvent) {
        List<Post> allPostsOfAuthor = postRepository.findAllByAuthorId(new AuthorId(accountDisabledEvent.accountId().getValue()));
        for (Post post : allPostsOfAuthor) {
            postService.makePostInvisible(post);
        }
    }

    @EventListener
    public void handleAccountActivatedEvent(AccountActivatedEvent accountActivatedEvent) {
        List<Post> allPostsOfAuthor = postRepository.findAllByIsVisibleFalseAndAuthorId(new AuthorId(accountActivatedEvent.accountId().getValue()));
        for (Post post : allPostsOfAuthor) {
            postService.makePostVisible(post);
        }
    }

    @EventListener
    public void handleAccountDeleted(AccountDeletedEvent accountDeletedEvent) {
        postRepository.deleteAllByAuthorId(new AuthorId(accountDeletedEvent.accountId().getValue()));
    }
}
