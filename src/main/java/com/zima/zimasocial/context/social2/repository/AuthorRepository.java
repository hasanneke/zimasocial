package com.zima.zimasocial.context.social2.repository;

import com.zima.zimasocial.context.social2.domain.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> { }
