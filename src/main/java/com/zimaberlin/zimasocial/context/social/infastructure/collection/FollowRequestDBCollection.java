package com.zimaberlin.zimasocial.context.social.infastructure.collection;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.authorrelation.FollowRequest;
import com.zimaberlin.zimasocial.context.social.authorrelation.FollowRequestCollection;
import com.zimaberlin.zimasocial.entity.FollowRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FollowRequestDBCollection implements FollowRequestCollection {
    private final FollowRequestDAOJpa followRequestDAOJpa;
    private final FollowRequestDBAdapter followRequestDBAdapter;
    @Override
    public void save(FollowRequest followRequest) {
        followRequestDAOJpa.save(new FollowRequestEntity(followRequest));
    }

    @Override
    public void delete(FollowRequest followRequest) {
        followRequestDAOJpa.deleteById(followRequest.getId());
    }

    @Override
    public Optional<FollowRequest> findById(UUID id) {
        Optional<FollowRequestEntity> followRequest = followRequestDAOJpa.findById(id);
        if(followRequest.isPresent()){
            return followRequest.map(followRequestDBAdapter::convertFollowRequestEntityToFollowRequest);
        }
        return Optional.empty();
    }

    @Override
    public List<FollowRequest> findAllByFollowedAuthorId(AuthorId id) {
        return followRequestDAOJpa.findByFollowedId(id.getId()).stream().map(followRequestDBAdapter::convertFollowRequestEntityToFollowRequest).toList();
    }

    @Override
    public Integer countByFollowedAuthorId(AuthorId id) {
        return followRequestDAOJpa.countByFollowedIdAndIsAcceptedFalse(id.getId());
    }

    @Override
    public Optional<FollowRequest> findFirstByFollowedAuthorIdOrderByCreatedAtDesc(AuthorId id) {
        Optional<FollowRequestEntity> followRequest = followRequestDAOJpa.findFirstByFollowedIdOrderByCreatedAtDesc(id.getId());
        if (followRequest.isPresent()){
            return followRequest.map(followRequestDBAdapter::convertFollowRequestEntityToFollowRequest);
        }
        return Optional.empty();
    }

    @Override
    public Integer countByFollowedAuthorIdAndIsApprovedFalse(AuthorId id) {
        return followRequestDAOJpa.countByFollowedIdAndIsAcceptedFalse(id.getId());
    }
}
