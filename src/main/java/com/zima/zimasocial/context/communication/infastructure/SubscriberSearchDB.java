package com.zima.zimasocial.context.communication.infastructure;

import com.zima.zimasocial.context.communication.domain.SubscriberSearch;
import com.zima.zimasocial.context.communication.domain.entity.RecipientDomain;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.entity.AuthorRelation;
import com.zima.zimasocial.context.social.author.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.entity.userRelation.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubscriberSearchDB implements SubscriberSearch {
    private final AuthorRelationRepository authorRelationRepository;
    private final AuthorRepository authorRepository;

    @Override
    public List<RecipientDomain> findSubscribers(RecipientId parent) {
        List<AuthorRelation> userRelationEntityList = authorRelationRepository.findAllByReceiverIdAndRelation(new AuthorId(parent.getValue()), Relation.followed);
        List<Author> userEntities = authorRepository.findAllById(userRelationEntityList.stream().map(AuthorRelation::getActorId).toList());
        return userEntities.stream()
                .map(UserEntityRecipientConverter::toRecipient)
                .toList();
    }
}
