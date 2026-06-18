package com.zima.zimasocial.context.social.author.application;

import com.zima.zimasocial.context.social.author.api.view.FollowRequestView;
import com.zima.zimasocial.context.social.author.api.adapter.FollowRequestViewAdapter;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.api.AuthorController;
import com.zima.zimasocial.context.social.author.api.adapter.AuthorViewAdapter;
import com.zima.zimasocial.context.social.author.api.view.AuthorView;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.entity.AuthorRelation;
import com.zima.zimasocial.context.social.author.entity.FollowRequest;
import com.zima.zimasocial.context.social.author.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.repository.FollowRequestRepository;
import com.zima.zimasocial.context.social.author.value.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class AuthorReadService {
    private final AuthorRepository authorRepository;
    private final AuthorRelationRepository authorRelationRepository;
    private final AuthorViewAdapter authorViewAdapter;
    private final FollowRequestRepository followRequestRepository;
    private final FollowRequestViewAdapter followRequestViewAdapter;

    public AuthorView getMe() {
        Author author = authorRepository.getAuthenticatedAuthor();
        return authorViewAdapter.toRichView(author);
    }

    public AuthorView getAuthor(String slug) {
        Author author = authorRepository.findBySlug(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        return authorViewAdapter.toRichView(author);
    }

    public PagedModel<AuthorView> findFollowers(String slug, int page, int size) throws NoSuchMethodException {
        Author viewer = authorRepository.findBySlug(slug).orElseThrow(AuthorNotFoundException::new);
        Page<AuthorView> followers =
                authorRelationRepository
                        .findAllByReceiverIdAndRelation(viewer.getId(), Relation.followed, PageRequest.of(page, size))
                        .map((e)->authorViewAdapter.toRichView(e.getActor()));
        PagedModel<AuthorView> pagedModel = PagedModel.of(
                followers.getContent(),
                new PagedModel.PageMetadata(followers.getSize(),
                        followers.getNumber(),
                        followers.getTotalElements(),
                        followers.getTotalPages()));

        Method method = AuthorController.class.getMethod("getFollowers",
                String.class,
                Integer.class,
                Integer.class);

        if(page < followers.getTotalPages()){
            Link link = linkTo(method, slug,page + 1, size).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, slug, page - 1, size).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return pagedModel;
    }

    public PagedModel<AuthorView> getFollowings(String slug, int page, int size) throws NoSuchMethodException {
        Author author = authorRepository.findBySlug(slug).orElseThrow(AuthorNotFoundException::new);
        Page<AuthorRelation> followersPage = authorRelationRepository.findAllByActorIdAndRelation(author.getId(), Relation.followed, PageRequest.of(page, size));
        List<AuthorView> authorViewList = followersPage.get().map((e)->authorViewAdapter.toRichView(e.getReceiver())).toList();
        PagedModel<AuthorView> pagedModel = PagedModel.of(
                authorViewList,
                new PagedModel.PageMetadata(followersPage.getSize(),
                        followersPage.getNumber(),
                        followersPage.getTotalElements(),
                        followersPage.getTotalPages()));

        Method method = AuthorController.class.getMethod("getFollowers",
                String.class,
                Integer.class,
                Integer.class);

        if(page < followersPage.getTotalPages()){
            Link link = linkTo(method, slug,page + 1, size).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, slug, page - 1, size).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return pagedModel;
    }

    public PagedModel<AuthorView> getBlocks(int page, int size) throws NoSuchMethodException {
        Author viewer = authorRepository.getAuthenticatedAuthor();
        Page<AuthorRelation> followersPage = authorRelationRepository.findAllByActorIdAndRelation(viewer.getId(),Relation.blocked, PageRequest.of(page, size));
        List<AuthorView> authorViewList = followersPage.get().map((e)->authorViewAdapter.toRichView(e.getReceiver())).toList();
        PagedModel<AuthorView> pagedModel = PagedModel.of(
                authorViewList,
                new PagedModel.PageMetadata(followersPage.getSize(),
                        followersPage.getNumber(),
                        followersPage.getTotalElements(),
                        followersPage.getTotalPages()));

        Method method = AuthorController.class.getMethod("getBlocks", Integer.class, Integer.class);

        if(page < followersPage.getTotalPages()){
            Link link = linkTo(method,page + 1, size).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, page - 1, size).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return pagedModel;
    }

    public PagedModel<AuthorView> searchAuthors(String query, int page, int size) throws NoSuchMethodException {
        Page<Author> userPage = authorRepository.searchUser(query, PageRequest.of(page, size));
        List<AuthorView> authorViewList = userPage.get().map(authorViewAdapter::toView).toList();
        PagedModel<AuthorView> pagedModel = PagedModel.of(
                authorViewList,
                new PagedModel.PageMetadata(userPage.getSize(),
                        userPage.getNumber(),
                        userPage.getTotalElements(),
                        userPage.getTotalPages()));

        Method method = AuthorController.class.getMethod("search",
                String.class,
                Integer.class,
                Integer.class);

        if(page < userPage.getTotalPages()){
            Link link = linkTo(method, query, page + 1, size).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, query, page - 1, size).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return pagedModel;
    }

    public List<FollowRequestView> getAllFollowRequests() {
        Author author = authorRepository.getAuthenticatedAuthor();
        List<FollowRequest> followRequests = followRequestRepository.findAllByFollowedId(author.getId());
        return followRequests.stream().map(followRequestViewAdapter::toView).toList();
    }
}

