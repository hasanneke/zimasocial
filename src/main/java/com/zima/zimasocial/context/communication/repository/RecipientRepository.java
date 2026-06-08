package com.zima.zimasocial.context.communication.repository;

import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.communication.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, RecipientId> {

}
