package com.zima.zimasocial.context.social2.api.adapter;

import com.zima.zimasocial.context.social2.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social2.api.views.FollowRequestView;
import com.zima.zimasocial.context.social2.domain.entity.Author;
import com.zima.zimasocial.context.social2.domain.entity.FollowRequest;
import com.zima.zimasocial.context.social2.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowRequestViewAdapter {
    private final AuthorRepository authorRepository;
    private final AuthorViewAdapter authorViewAdapter;

    public FollowRequestView toView(FollowRequest followRequest) {
        Author follower = authorRepository.findById(followRequest.getFollowerId()).orElseThrow(() ->new AuthorNotFoundException(followRequest.getFollowerId().getValue()));
        Author followed = authorRepository.findById(followRequest.getFollowedId()).orElseThrow(() ->new AuthorNotFoundException(followRequest.getFollowedId().getValue()));

        return new FollowRequestView(followRequest, authorViewAdapter.toRichView(follower), authorViewAdapter.toRichView(followed));
    }
}
