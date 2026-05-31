package com.zima.zimasocial.context.social2.domain.service;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.repository.AuthorRelationRepository;
import com.zima.zimasocial.entity.userRelation.Relation;
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
