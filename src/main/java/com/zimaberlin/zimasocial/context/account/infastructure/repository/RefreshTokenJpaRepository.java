package com.zimaberlin.zimasocial.context.account.infastructure.repository;

import com.zimaberlin.zimasocial.context.account.infastructure.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByTokenAndRevoked(String token, boolean revoked);
    Optional<RefreshTokenEntity> findFirstByUserIdAndRevokedIsFalseAndExpiresAtGreaterThanOrderByCreatedAtDesc(Long userId, LocalDateTime expireDate);
}
