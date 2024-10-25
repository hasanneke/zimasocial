package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> { }
