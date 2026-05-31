package com.zima.zimasocial.context.social.media.repository;

import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.media.value.MediaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, MediaId> {
}
