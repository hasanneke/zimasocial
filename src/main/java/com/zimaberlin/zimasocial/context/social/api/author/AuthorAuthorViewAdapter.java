package com.zimaberlin.zimasocial.context.social.api.author;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.authorrelation.AuthorRelationCollection;
import com.zimaberlin.zimasocial.context.social.authorrelation.BlockRelation;
import com.zimaberlin.zimasocial.context.social.authorrelation.FollowRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorAuthorViewAdapter {
    private final AuthorRelationCollection authorRelationRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorAuthorViewAdapter(AuthorRelationCollection authorRelationRepository, AuthorRepository authorRepository) {
        this.authorRelationRepository = authorRelationRepository;
        this.authorRepository = authorRepository;
    }

    public AuthorView authorViewFromAuthor(Author author) {
        Author authenticatedUser = authorRepository.getAuthenticatedAuthor();
        Optional<FollowRelation> followRelation =
                authorRelationRepository
                        .findFollowRelationBetween(authenticatedUser.getId(), author.getId());
        Optional<BlockRelation> blockRelation =
                authorRelationRepository.findBlockRelationBetween(authenticatedUser.getId(), author.getId());

        AuthorView authorView = new AuthorView();
        authorView.setId(author.getId().getId());
        authorView.setSlug(author.getSlug());
        authorView.setName(author.getName());
        authorView.setFamilyName(author.getFamilyName());
        authorView.setAvatarUrl(author.getAvatarFileName());
        authorView.setBio(author.getBio());
        authorView.setFollowerCount(author.getFollowersCount());
        authorView.setFollowingCount(author.getFollowingCount());
        authorView.setFollowed(followRelation.isPresent());
        authorView.setIsPrivate(author.getIsPrivate());
        authorView.setIsBlocked(blockRelation.isPresent());
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
