package com.zimaberlin.zimasocial.context.social.api.author;

import com.zimaberlin.zimasocial.context.account.entity.Account;
import com.zimaberlin.zimasocial.context.account.repository.AccountRepository;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.authorrelation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorAuthorViewAdapter {
    private final AuthorRelationCollection authorRelationRepository;
    private final AccountRepository accountRepository;
    private final FollowRequestCollection followRequestCollection;
    private final AuthorRepository authorRepository;

    public AuthorView authorViewFromAuthor(Author author) {
        Author authenticatedUser = authorRepository.getAuthenticatedAuthor();
        Account account = accountRepository.getAuthenticatedAccount();
        Optional<FollowRelation> followRelation =
                authorRelationRepository
                        .findFollowRelationBetween(authenticatedUser.getId(), author.getId());
        Optional<FollowRelation> followMeRelation =
                authorRelationRepository
                        .findFollowRelationBetween(author.getId(), authenticatedUser.getId());
        Optional<BlockRelation> blockRelation =
                authorRelationRepository.findBlockRelationBetween(authenticatedUser.getId(), author.getId());
        Optional<FollowRequest> followRequestReceived =
                followRequestCollection.findByFollowedIdAndFollowerId(authenticatedUser.getId(), author.getId());
        Optional<FollowRequest> followRequestSent =
                followRequestCollection.findByFollowedIdAndFollowerId(author.getId(), authenticatedUser.getId());
        AuthorView authorView = new AuthorView();
        authorView.setId(author.getId().getValue());
        authorView.setSlug(author.getSlug());
        authorView.setName(author.getName());
        authorView.setFamilyName(author.getFamilyName());
        authorView.setAvatarUrl(author.getAvatarFileName());
        authorView.setBio(author.getBio());
        authorView.setFollowerCount(author.getFollowersCount());
        authorView.setFollowingCount(author.getFollowingCount());
        authorView.setFollowed(followRelation.isPresent());
        authorView.setFollowingMe(followMeRelation.isPresent());
        authorView.setFollowRequestSent(followRequestSent.isPresent());
        authorView.setFollowRequestReceived(followRequestReceived.isPresent());
        authorView.setIsPrivate(author.getIsPrivate());
        authorView.setIsBlocked(blockRelation.isPresent());
        authorView.setTermsOfUseAccepted(account.getTermsOfUseAccepted());
        authorView.addLinks();
        return authorView;
    }

    public DetailedAuthorView detailedAuthorViewFromAuthor(Author author) {
        AuthorView authorView = authorViewFromAuthor(author);
        DetailedAuthorView detailedAuthorView = new DetailedAuthorView();
        detailedAuthorView.mergeAuthorView(authorView);
        detailedAuthorView.setEmail(author.getEmail());
        return detailedAuthorView;
    }
}
