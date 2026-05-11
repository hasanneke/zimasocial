package com.zima.zimasocial.context.social.api.author;

import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.application.AuthorService;
import com.zima.zimasocial.context.social.authorrelation.AuthorRelationCollection;
import com.zima.zimasocial.context.social.authorrelation.entity.FollowRequest;
import com.zima.zimasocial.context.social.authorrelation.FollowRequestCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AuthorControllerBridge {
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;
    private final AuthorRelationCollection authorRelationRepository;
    private final AuthorAuthorViewAdapter authorAuthorViewMapper;
    private final FollowRequestCollection followRequestCollection;
    private final FollowRequestFollowRequestDTOAdapter followRequestFollowRequestDTOAdapter;
    @Autowired
    public AuthorControllerBridge(AuthorRepository authorRepository, AuthorService authorService, AuthorRelationCollection authorRelationRepository, AuthorAuthorViewAdapter authorAuthorViewMapper, FollowRequestCollection followRequestCollection, FollowRequestFollowRequestDTOAdapter followRequestFollowRequestDTOAdapter) {
        this.authorRepository = authorRepository;
        this.authorRelationRepository = authorRelationRepository;
        this.authorAuthorViewMapper = authorAuthorViewMapper;
        this.followRequestCollection = followRequestCollection;
        this.followRequestFollowRequestDTOAdapter = followRequestFollowRequestDTOAdapter;
        this.authorService = authorService;
    }

    public DetailedAuthorView getMe() {
        Author author = authorRepository.getAuthenticatedAuthor();
        return authorAuthorViewMapper.detailedAuthorViewFromAuthor(author);
    }

    public AuthorView getAuthor(String slug) {
        Author author = authorRepository.findBySlugAndIsDisabledFalseAndNotBeingBlocked(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        return authorAuthorViewMapper.authorViewFromAuthor(author);
    }

    PagedModel<AuthorView> getFollowers(String slug, int page, int size) throws NoSuchMethodException {
        Page<Author> followersPage = authorRelationRepository.findFollowers(slug, page, size);
        List<AuthorView> authorViewList = followersPage.get().map(authorAuthorViewMapper::authorViewFromAuthorLight).toList();
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

    PagedModel<AuthorView> getFollowings(String slug, int page, int size) throws NoSuchMethodException {
        Page<Author> followersPage = authorRelationRepository.findFollowings(slug, page, size);
        List<AuthorView> authorViewList = followersPage.get().map(authorAuthorViewMapper::authorViewFromAuthorLight).toList();
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

    PagedModel<AuthorView> getBlocks(int page, int size) throws NoSuchMethodException {
        Page<Author> followersPage = authorRelationRepository.findBlocks(page, size);
        List<AuthorView> authorViewList = followersPage.get().map(authorAuthorViewMapper::authorViewFromAuthor).toList();
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

    PagedModel<AuthorView> searchAuthors(String query, int page, int size) throws NoSuchMethodException {
        Page<Author> userPage = authorRepository.search(query, page, size);
        List<AuthorView> authorViewList = userPage.get().map(authorAuthorViewMapper::authorViewFromAuthor).toList();
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

    List<FollowRequestDTO> getAllFollowRequests() {
        Author author = authorRepository.getAuthenticatedAuthor();
        List<FollowRequest> followRequests = followRequestCollection.findAllByFollowedAuthorIdAndUpdatedAtIsNull(author.getId());
        return followRequests.stream().map(followRequestFollowRequestDTOAdapter::followRequestDTOFromFollowRequest).toList();
    }

    FollowRequestsInfo getFollowRequestInfo(String followedAuthorSlug) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Optional<FollowRequest> latestFollowRequest = followRequestCollection.findFirstByFollowedAuthorIdOrderByCreatedAtDesc(author.getId());
        Integer waitingFollowRequestsCount = followRequestCollection.countByFollowedAuthorId(author.getId());
        if(latestFollowRequest.isPresent()){
            Author follower = authorRepository.findById(latestFollowRequest.get().getFollowerAuthorId()).orElse(null);
            if(follower != null){
                return new FollowRequestsInfo(waitingFollowRequestsCount, authorAuthorViewMapper.authorViewFromAuthor(follower));
            }
        }
        return new FollowRequestsInfo(waitingFollowRequestsCount);
    }

    void acceptFollowRequest(String followerSlug) {
        authorService.acceptFollowRequest(followerSlug);
    }
}

