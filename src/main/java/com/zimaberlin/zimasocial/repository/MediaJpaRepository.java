package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.media.MediaJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MediaJpaRepository extends JpaRepository<MediaJpa, UUID> {
    @Query(value = "SELECT gen_random_uuid()", nativeQuery = true)
    UUID nextUUID();
}
