package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndAuthProvider(String email, String authProvider);
    Optional<UserEntity> findBySlugAndIsDeletedIn(String slug, List<Boolean> deletedOptions);
    @Query(value = "SELECT * FROM users WHERE slug = :slug", nativeQuery = true)
    Optional<UserEntity> findBySlugWithDeletedUsers(String slug);
    Optional<UserEntity> findBySlug(String slug);
    @Query("SELECT u FROM UserEntity u " +
            "INNER JOIN u.followers f " +
            "WHERE f.id = :userId")
    Page<UserEntity> findFollowersByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT u FROM UserEntity u "
            + "INNER JOIN followers f "
            + "WHERE f.id = :userId")
    Page<UserEntity> findFollowingsByUserId(@Param("userId") Long userId, Pageable pageable);
}
