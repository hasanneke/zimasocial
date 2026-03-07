package com.zima.zimasocial.context.communication.infastructure;

import com.zima.zimasocial.context.communication.domain.SubscriberSearch;
import com.zima.zimasocial.context.communication.domain.entity.Recipient;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.social.author.application.AuthorService;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.entity.userRelation.Relation;
import com.zima.zimasocial.entity.userRelation.UserRelationEntity;
import com.zima.zimasocial.repository.UserJpaRepository;
import com.zima.zimasocial.repository.UserRelationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubscriberSearchDB implements SubscriberSearch {
    private final UserRelationJpaRepository userRelationJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final AuthorService authorService;

    @Override
    public List<Recipient> findSubscribers(RecipientId parent) {
        List<UserRelationEntity> userRelationEntityList = userRelationJpaRepository.findAllByReceiverIdAndRelation(parent.getValue(), Relation.followed);
        List<UserEntity> userEntities = userJpaRepository.findAllById(userRelationEntityList.stream().map(UserRelationEntity::getActorId).toList());
        return userEntities.stream()
                .map(UserEntityRecipientConverter::toRecipient)
                .toList();
    }
}
