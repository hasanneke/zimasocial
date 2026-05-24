package com.zima.zimasocial.context.social.listener;

import com.zima.zimasocial.context.account.event.AccountActivatedEvent;
import com.zima.zimasocial.context.account.event.AccountDeletedEvent;
import com.zima.zimasocial.context.account.event.AccountDisabledEvent;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.comment.CommentDomainRepository;
import com.zima.zimasocial.context.social.post.application.PostService;
import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.context.social.post.repository.PostDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountEventListener {
    private final PostDomainRepository postRepository;
    private final CommentDomainRepository commentRepository;
    private final PostService postService;

    @EventListener
    public void handleAccountDisabledEvent(AccountDisabledEvent accountDisabledEvent) {
        List<PostDomain> allPostsOfAuthor = postRepository.findAllByAuthorId(new AuthorDomainId(accountDisabledEvent.accountId().getValue()));
        for (PostDomain post : allPostsOfAuthor) {
            postService.makePostInvisible(post);
        }
    }

    @EventListener
    public void handleAccountActivatedEvent(AccountActivatedEvent accountActivatedEvent) {
        List<PostDomain> allPostsOfAuthor = postRepository.findAllInvisiblePostsByAuthorId(new AuthorDomainId(accountActivatedEvent.accountId().getValue()));
        for (PostDomain post : allPostsOfAuthor) {
            postService.makePostVisible(post);
        }
    }

    @EventListener
    public void handleAccountDeleted(AccountDeletedEvent accountDeletedEvent) {
        List<PostDomain> allPostsOfAuthor = postRepository.findAllByAuthorId(new AuthorDomainId(accountDeletedEvent.accountId().getValue()));
        for (PostDomain post : allPostsOfAuthor) {
            postService.delete(post.getPostId());
        }
    }
}
