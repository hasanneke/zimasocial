package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByEmailAndAuthProvider(String email, String authProvider);
}
