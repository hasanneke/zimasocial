package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndAuthProvider(String email, String authProvider);
    Optional<UserEntity> findBySlug(String slug);
}
