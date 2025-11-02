package com.zimaberlin.zimasocial.context.social.api.author;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorNotFoundException;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.authorrelation.FollowRequest;
import org.springframework.stereotype.Component;

@Component
public class FollowRequestFollowRequestDTOAdapter {
    private final AuthorRepository authorRepository;
    private final AuthorAuthorViewAdapter authorViewAdapter;

    public FollowRequestFollowRequestDTOAdapter(AuthorRepository authorRepository, AuthorAuthorViewAdapter authorViewAdapter) {
        this.authorRepository = authorRepository;
        this.authorViewAdapter = authorViewAdapter;
    }

    FollowRequestDTO followRequestDTOFromFollowRequest(FollowRequest followRequest) {
        Author follower = authorRepository.findById(followRequest.getFollowerAuthorId()).orElseThrow(() ->new AuthorNotFoundException(followRequest.getFollowerAuthorId().getValue()));
        Author followed = authorRepository.findById(followRequest.getFollowedAuthorId()).orElseThrow(() ->new AuthorNotFoundException(followRequest.getFollowedAuthorId().getValue()));

        return new FollowRequestDTO(followRequest, authorViewAdapter.authorViewFromAuthor(follower), authorViewAdapter.authorViewFromAuthor(followed));
    }
}
