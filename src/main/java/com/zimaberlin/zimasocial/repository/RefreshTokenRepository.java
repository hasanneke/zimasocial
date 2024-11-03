package com.zimaberlin.zimasocial.repository;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.zimaberlin.zimasocial.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByTokenAndRevoked(String token, boolean revoked);
}
