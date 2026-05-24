package com.zima.zimasocial.context.social.api.author;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.authorrelation.AuthorRelationCollection;
import com.zima.zimasocial.context.social.authorrelation.FollowRequestCollection;
import com.zima.zimasocial.context.social.authorrelation.entity.FollowRequest;
import com.zima.zimasocial.context.social.authorrelation.values.BlockRelation;
import com.zima.zimasocial.context.social.authorrelation.values.FollowRelation;
import com.zima.zimasocial.context.social.playlist.api.dto.PlaylistDTO;
import com.zima.zimasocial.context.social.playlist.application.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorAuthorViewAdapter {
    private final AuthorRelationCollection authorRelationRepository;
    private final AccountRepository accountRepository;
    private final FollowRequestCollection followRequestCollection;
    private final AuthorRepository authorRepository;
    private final PlayListService playListService;

    public AuthorView authorViewFromAuthor(AuthorDomain author, Boolean fetchPlaylist) {
        AuthorView authorView = new AuthorView();
        AuthorDomain authenticatedUser = authorRepository.getAuthenticatedAuthor();
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
        if(fetchPlaylist){
            List<PlaylistDTO> playlists = playListService.getAllList(author.getSlug());
            authorView.setPlaylists(playlists);
        }
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
        return authorView;
    }

    public AuthorView authorViewFromAuthorLight(AuthorDomain author) {
        AuthorView authorView = new AuthorView();
        authorView.setId(author.getId().getValue());
        authorView.setSlug(author.getSlug());
        authorView.setName(author.getName());
        authorView.setFamilyName(author.getFamilyName());
        authorView.setAvatarUrl(author.getAvatarFileName());
        authorView.setBio(author.getBio());
        authorView.setFollowerCount(author.getFollowersCount());
        authorView.setFollowingCount(author.getFollowingCount());
        authorView.setIsPrivate(author.getIsPrivate());
        return authorView;
    }

    public AuthorView authorViewFromAuthor(AuthorDomain author){
        return authorViewFromAuthor(author, true);
    }

    public DetailedAuthorView detailedAuthorViewFromAuthor(AuthorDomain author) {
        AuthorView authorView = authorViewFromAuthor(author);
        DetailedAuthorView detailedAuthorView = new DetailedAuthorView();
        detailedAuthorView.mergeAuthorView(authorView);
        detailedAuthorView.setEmail(author.getEmail());
        return detailedAuthorView;
    }
}
