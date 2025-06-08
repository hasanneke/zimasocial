package com.zimaberlin.zimasocial.context.social.api.author;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorNotFoundException;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.author.AuthorService;
import com.zimaberlin.zimasocial.context.social.authorrelation.AuthorRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AuthorControllerBridge {
    private AuthorService authorService;
    private AuthorRepository authorRepository;
    private AuthorRelationRepository authorRelationRepository;
    private AuthorAuthorViewAdapter authorAuthorViewMapper;

    @Autowired
    public AuthorControllerBridge(AuthorService authorService, AuthorRepository authorRepository, AuthorRelationRepository authorRelationRepository, AuthorAuthorViewAdapter authorAuthorViewMapper) {
        this.authorService = authorService;
        this.authorRepository = authorRepository;
        this.authorRelationRepository = authorRelationRepository;
        this.authorAuthorViewMapper = authorAuthorViewMapper;
    }

    public DetailedAuthorView getMe() {
        Author author = authorRepository.getAuthenticatedAuthor();
        return authorAuthorViewMapper.detailedAuthorViewFromAuthor(author);
    }

    public AuthorView getAuthor(String slug) {
        Author author = authorRepository.findBySlug(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        return authorAuthorViewMapper.authorViewFromAuthor(author);
    }

    PagedModel<AuthorView> getFollowers(String slug, int page, int size) throws NoSuchMethodException {
        Page<Author> followersPage = authorRelationRepository.findFollowers(slug, page, size);
        List<AuthorView> authorViewList = followersPage.get().map(e-> authorAuthorViewMapper.authorViewFromAuthor(e)).toList();
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
        List<AuthorView> authorViewList = followersPage.get().map(e-> authorAuthorViewMapper.authorViewFromAuthor(e)).toList();
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

    PagedModel<AuthorView> searchAuthors(String query, int page, int size) throws NoSuchMethodException {
        Page<Author> userPage = authorRepository.search(query, page, size);
        List<AuthorView> authorViewList = userPage.get().map(e-> authorAuthorViewMapper.authorViewFromAuthor(e)).toList();
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
}

