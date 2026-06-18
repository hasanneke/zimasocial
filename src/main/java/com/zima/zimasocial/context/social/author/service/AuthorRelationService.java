package com.zima.zimasocial.context.social.author.service;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social.author.value.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorRelationService {
    private final AuthorRelationRepository authorRelationRepository;
    public boolean isAuthorFollowed(AuthorId followerId, AuthorId followedId) {
        return authorRelationRepository.findFirstByActorIdAndReceiverIdAndRelation(followerId, followedId, Relation.followed).isPresent();
    }
}
