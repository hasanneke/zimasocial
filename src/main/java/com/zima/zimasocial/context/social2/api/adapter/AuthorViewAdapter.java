package com.zima.zimasocial.context.social2.api.adapter;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.social.api.author.AuthorView;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.authorrelation.AuthorRelationCollection;
import com.zima.zimasocial.context.social.authorrelation.FollowRequestCollection;
import com.zima.zimasocial.context.social.authorrelation.entity.FollowRequest;
import com.zima.zimasocial.context.social.authorrelation.values.BlockRelation;
import com.zima.zimasocial.context.social.authorrelation.values.FollowRelation;
import com.zima.zimasocial.context.social2.domain.entity.Author;
import com.zima.zimasocial.context.social2.repository.AuthorRepository;
import com.zima.zimasocial.utility.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorViewAdapter {
    private final AuthorRepository authorRepository;
    private final AccountRepository accountRepository;
    private final AuthorRelationCollection authorRelationRepository;
    private final FollowRequestCollection followRequestCollection;
    public AuthorView toView(Author author) {
        AuthorView authorView = new AuthorView();
        Author authenticatedUser = authorRepository.findById(CurrentUser.getCurrentUserId()).orElseThrow(AuthorNotFoundException::new);
        Account account = accountRepository.getAuthenticatedAccount();
        Optional<FollowRelation> followRelation =
                authorRelationRepository
                        .findFollowRelationBetween(new AuthorDomainId(authenticatedUser.getId().getValue()), new AuthorDomainId(author.getId().getValue()));
        Optional<FollowRelation> followMeRelation =
                authorRelationRepository
                        .findFollowRelationBetween(new AuthorDomainId(author.getId().getValue()), new AuthorDomainId(authenticatedUser.getId().getValue()));
        Optional<BlockRelation> blockRelation =
                authorRelationRepository.findBlockRelationBetween(new AuthorDomainId(authenticatedUser.getId().getValue()), new AuthorDomainId(author.getId().getValue()));
        Optional<FollowRequest> followRequestReceived =
                followRequestCollection.findByFollowedIdAndFollowerId(new AuthorDomainId(authenticatedUser.getId().getValue()), new AuthorDomainId(author.getId().getValue()));
        Optional<FollowRequest> followRequestSent =
                followRequestCollection.findByFollowedIdAndFollowerId(new AuthorDomainId(author.getId().getValue()), new AuthorDomainId(authenticatedUser.getId().getValue()));

        authorView.setId(author.getId().getValue());
        authorView.setSlug(author.getSlug());
        authorView.setName(author.getName());
        authorView.setFamilyName(author.getFamilyName());
        authorView.setAvatarUrl(author.getAvatarFileName());
        authorView.setBio(author.getBio());
        authorView.setFollowerCount(author.getFollowerCount());
        authorView.setFollowingCount(author.getFollowingCount());
        authorView.setFollowed(followRelation.isPresent());
        authorView.setFollowingMe(followMeRelation.isPresent());
        authorView.setFollowRequestSent(followRequestSent.isPresent());
        authorView.setFollowRequestReceived(followRequestReceived.isPresent());
        authorView.setIsPrivate(author.isPrivate());
        authorView.setIsBlocked(blockRelation.isPresent());
        authorView.setTermsOfUseAccepted(account.getTermsOfUseAccepted());
        return authorView;
    }
}
