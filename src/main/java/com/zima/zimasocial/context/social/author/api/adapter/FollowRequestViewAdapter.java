package com.zima.zimasocial.context.social.author.api.adapter;

import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.api.view.FollowRequestView;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.entity.FollowRequest;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
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
