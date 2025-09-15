package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndAuthProvider(String email, String authProvider);
    Optional<UserEntity> findBySlugAndIsDeletedIn(String slug, List<Boolean> deletedOptions);
    @Query(value = "SELECT * FROM users WHERE slug = :slug AND IS_DELETED = FALSE AND IS_DISABLED = FALSE", nativeQuery = true)
    Optional<UserEntity> findBySlugWithDeletedUsers(String slug);
    Optional<UserEntity> findBySlug(String slug);
    Optional<UserEntity> findBySlugAndIsDisabledFalse(String slug);
    @Query(value = "SELECT * FROM USERS u WHERE (u.name ILIKE %:query% OR u.slug ILIKE %:query%) AND IS_DELETED = false AND IS_DISABLED = false", nativeQuery = true)
    Page<UserEntity> searchUser(String query, Pageable pageable);

    @Query(value = "SELECT nextval('user_sequence')")
    Long nextId();
}
