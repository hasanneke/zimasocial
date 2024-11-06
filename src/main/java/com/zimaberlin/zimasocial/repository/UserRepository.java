package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndAuthProvider(String email, String authProvider);
    Optional<UserEntity> findBySlug(String slug);
    @Query(value = "SELECT u.* FROM users u " +
            "INNER JOIN followers f ON u.id = f.following_id " +
            "WHERE f.follower_id = :userId", nativeQuery = true)
    Page<UserEntity> findFollowersByUserIdWithNativeQuery(String userId);

    @Query("SELECT u FROM User u " +
            "INNER JOIN u.followers f " +
            "WHERE f.id = :userId")
    Page<UserEntity> findFollowersByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT u FROM users "
            + "INNER JOIN followers f "
            + "WHERE f.id = :userid")
    Page<UserEntity> findFollowingsByUserId(@Param("userId") Long userId, Pageable pageable);
}
