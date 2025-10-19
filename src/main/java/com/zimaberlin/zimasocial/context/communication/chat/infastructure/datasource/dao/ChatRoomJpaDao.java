package com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.dao;

import com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.entity.ChatRoomJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface ChatRoomJpaDao extends JpaRepository<ChatRoomJpaEntity, UUID> {
    Page<ChatRoomJpaEntity> findByParticipant1OrParticipant2(Long participant1Id, Long participant2Id, PageRequest pageRequest);
    @Query("""
            SELECT chatRoom FROM ChatRoomJpaEntity chatRoom WHERE
            (chatRoom.participant1Id = :participant1Id AND chatRoom.participant2Id = :participant2Id)
            OR
            (chatRoom.participant1Id = :participant2Id AND chatRoom.participant1Id = :participant2Id)
    """)
    Optional<ChatRoomJpaEntity> findBetween(@Param("participant1Id") Long participant1Id,
                                            @Param("participant2Id")  Long participant2Id);
}
