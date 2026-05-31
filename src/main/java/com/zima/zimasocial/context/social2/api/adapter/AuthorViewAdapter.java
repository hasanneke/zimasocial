package com.zima.zimasocial.context.social2.api.adapter;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.social2.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social2.api.views.AuthorView;
import com.zima.zimasocial.context.social2.domain.entity.Author;
import com.zima.zimasocial.context.social2.domain.entity.AuthorRelation;
import com.zima.zimasocial.context.social2.domain.entity.FollowRequest;
import com.zima.zimasocial.context.social2.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social2.repository.AuthorRepository;
import com.zima.zimasocial.context.social2.repository.FollowRequestRepository;
import com.zima.zimasocial.entity.userRelation.Relation;
import com.zima.zimasocial.utility.CurrentUser;
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
