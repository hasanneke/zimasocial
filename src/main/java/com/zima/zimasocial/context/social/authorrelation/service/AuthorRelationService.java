package com.zima.zimasocial.context.social.authorrelation.service;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.authorrelation.AuthorRelationCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorRelationService {
    private final AuthorRelationCollection authorRelationCollection;
    public boolean isAuthorFollowed(AuthorDomainId followerId, AuthorDomainId followedId) {
        return authorRelationCollection.findFollowRelationBetween(followerId, followedId).isPresent();
    }
}
