package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.context.social.infastructure.repository.MediaItemJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MediaManager {
    private final Map<String, MediaSearcher> mediaSearchers;
    private final MediaItemJpaRepository mediaItemJpaRepository;
    @Autowired
    public MediaManager(Map<String, MediaSearcher> mediaSearchers, MediaItemJpaRepository mediaItemJpaRepository) {
        this.mediaSearchers = mediaSearchers;
        this.mediaItemJpaRepository = mediaItemJpaRepository;
    }
    public Optional<UUID> getMedia(String resourceId, String provider) {
        return  mediaItemJpaRepository.findIdByResourceIdAndProvider(resourceId, provider);
    }
    public MediaSearcher searcher(String provider) {
        MediaSearcher mediaSearcher = mediaSearchers.get(provider);
        if (mediaSearcher == null) {
            throw new IllegalArgumentException("Unknown processor: " + mediaSearcher);
        }
        return mediaSearcher;
    }
}
