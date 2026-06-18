package com.zima.zimasocial.context.social.author.api.adapter;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.social.author.api.view.AuthorView;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.entity.AuthorRelation;
import com.zima.zimasocial.context.social.author.entity.FollowRequest;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.repository.FollowRequestRepository;
import com.zima.zimasocial.context.social.author.value.Relation;
import com.zima.zimasocial.shared.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorViewAdapter {
    private final AuthorRepository authorRepository;
    private final AccountRepository accountRepository;
    private final AuthorRelationRepository authorRelationRepository;
    private final FollowRequestRepository followRequestRepository;
    public AuthorView toRichView(Author author) {
        AuthorView authorView = toView(author);
        Author authenticatedUser = authorRepository.findById(CurrentUser.getCurrentUserId()).orElseThrow(AuthorNotFoundException::new);
        Account account = accountRepository.getAuthenticatedAccount();
        Optional<AuthorRelation> followRelation =
                authorRelationRepository
                        .findByActorAndReceiverAndRelation(authenticatedUser, author, Relation.followed);
        Optional<AuthorRelation> followMeRelation =
                authorRelationRepository
                        .findByActorAndReceiverAndRelation(authenticatedUser,author,Relation.followed);
        Optional<AuthorRelation> blockRelation =
                authorRelationRepository.findByActorAndReceiverAndRelation(authenticatedUser, author, Relation.blocked);
        Optional<FollowRequest> followRequestReceived =
                followRequestRepository.findByFollowerIdAndFollowedId(author.getId(), authenticatedUser.getId());
        Optional<FollowRequest> followRequestSent =
                followRequestRepository.findByFollowerIdAndFollowedId(authenticatedUser.getId(), author.getId());
        authorView.setFollowed(followRelation.isPresent());
        authorView.setFollowingMe(followMeRelation.isPresent());
        authorView.setFollowRequestSent(followRequestSent.isPresent());
        authorView.setFollowRequestReceived(followRequestReceived.isPresent());
        authorView.setIsBlocked(blockRelation.isPresent());
        authorView.setTermsOfUseAccepted(account.getTermsOfUseAccepted());
        return authorView;
    }

    public AuthorView toView(Author author) {
        AuthorView authorView = new AuthorView();
        authorView.setId(author.getId().getValue());
        authorView.setSlug(author.getSlug());
        authorView.setName(author.getName());
        authorView.setFamilyName(author.getFamilyName());
        authorView.setAvatarUrl(author.getAvatarFileName());
        authorView.setBio(author.getBio());
        authorView.setFollowerCount(author.getFollowerCount());
        authorView.setFollowingCount(author.getFollowingCount());
        authorView.setIsPrivate(author.getIsPrivate());
        return authorView;
    }
}
