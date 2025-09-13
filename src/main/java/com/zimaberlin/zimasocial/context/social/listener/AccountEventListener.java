package com.zimaberlin.zimasocial.context.social.listener;

import com.zimaberlin.zimasocial.context.account.event.AccountActivatedEvent;
import com.zimaberlin.zimasocial.context.account.event.AccountDeletedEvent;
import com.zimaberlin.zimasocial.context.account.event.AccountDisabledEvent;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.context.social.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountEventListener {
    private final PostRepository postRepository;
    private final PostService postService;
    @EventListener
    public void handleAccountDisabledEvent(AccountDisabledEvent accountDisabledEvent) {
        List<Post> allPostsOfAuthor = postRepository.findAllByAuthorId(new AuthorId(accountDisabledEvent.accountId().getValue()));
        for (Post post : allPostsOfAuthor) {
            postService.makePostInvisible(post);
        }
    }

    @EventListener
    public void handleAccountActivatedEvent(AccountActivatedEvent accountActivatedEvent) {
        List<Post> allPostsOfAuthor = postRepository.findAllInvisiblePostsByAuthorId(new AuthorId(accountActivatedEvent.accountId().getValue()));
        for (Post post : allPostsOfAuthor) {
            postService.makePostVisible(post);
        }
    }

    @EventListener
    public void handleAccountDeleted(AccountDeletedEvent accountDeletedEvent) {
        List<Post> allPostsOfAuthor = postRepository.findAllByAuthorId(new AuthorId(accountDeletedEvent.accountId().getValue()));
        for (Post post : allPostsOfAuthor) {
            postService.delete(post.getPostId());
        }
    }
}
