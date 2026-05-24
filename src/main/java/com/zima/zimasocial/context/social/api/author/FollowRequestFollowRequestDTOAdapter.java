package com.zima.zimasocial.context.social.api.author;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRepositoryDomain;
import com.zima.zimasocial.context.social.authorrelation.entity.FollowRequest;
import org.springframework.stereotype.Component;

@Component
public class FollowRequestFollowRequestDTOAdapter {
    private final AuthorRepositoryDomain authorRepository;
    private final AuthorAuthorViewAdapter authorViewAdapter;

    public FollowRequestFollowRequestDTOAdapter(AuthorRepositoryDomain authorRepository, AuthorAuthorViewAdapter authorViewAdapter) {
        this.authorRepository = authorRepository;
        this.authorViewAdapter = authorViewAdapter;
    }

    FollowRequestDTO followRequestDTOFromFollowRequest(FollowRequest followRequest) {
        AuthorDomain follower = authorRepository.findById(followRequest.getFollowerAuthorId()).orElseThrow(() ->new AuthorNotFoundException(followRequest.getFollowerAuthorId().getValue()));
        AuthorDomain followed = authorRepository.findById(followRequest.getFollowedAuthorId()).orElseThrow(() ->new AuthorNotFoundException(followRequest.getFollowedAuthorId().getValue()));

        return new FollowRequestDTO(followRequest, authorViewAdapter.authorViewFromAuthor(follower), authorViewAdapter.authorViewFromAuthor(followed));
    }
}
