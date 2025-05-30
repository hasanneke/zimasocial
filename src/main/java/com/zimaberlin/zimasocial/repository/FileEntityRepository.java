package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.file.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByName(String fileName);
}
