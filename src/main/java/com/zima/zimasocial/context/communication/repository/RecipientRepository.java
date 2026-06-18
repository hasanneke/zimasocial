package com.zima.zimasocial.context.communication.repository;

import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.communication.entity.Recipient;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.shared.CurrentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, RecipientId> {
    @Query(
        """
           SELECT recipient FROM Recipient recipient
           LEFT JOIN FETCH recipient.deviceTokens
           WHERE recipient.id.value IN (
            SELECT authorRelation.actorId.value FROM AuthorRelation authorRelation
            WHERE authorRelation.receiverId.value = :parentId
           )     
        """
    )
    List<Recipient> findAllSubscribers(Long parentId);

    default Recipient getAuthenticatedRecipient() {
        return findById(new RecipientId(CurrentUser.getCurrentUserId().getValue())).orElseThrow(AuthorNotFoundException::new);
    }
}
