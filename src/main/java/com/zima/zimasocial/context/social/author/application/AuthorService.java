package com.zima.zimasocial.context.social.author.application;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.exception.*;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.value.AuthorFollowedEvent;
import com.zima.zimasocial.context.social.authorrelation.*;
import com.zima.zimasocial.context.social.authorrelation.values.BlockRelation;
import com.zima.zimasocial.context.social.authorrelation.values.FollowRelation;
import com.zima.zimasocial.context.social.authorrelation.entity.FollowRequest;
import com.zima.zimasocial.context.social.image.ImageService;
import com.zima.zimasocial.exception.BadRequestException;
import com.zima.zimasocial.exception.UnauthorizedException;
import com.zima.zimasocial.shared.StaticEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorRelationCollection authorRelationRepository;
    private final ImageService imageService;
    private final FollowRequestCollection followRequestCollection;
    @Autowired
    public AuthorService(AuthorRepository authorRepository, AuthorRelationCollection authorRelationRepository, ImageService imageService, FollowRequestCollection followRequestCollection) {
        this.authorRepository = authorRepository;
        this.authorRelationRepository = authorRelationRepository;
        this.imageService = imageService;
        this.followRequestCollection = followRequestCollection;
    }

    @Transactional
    public void requestToFollow(String slug) {
        Author follower = authorRepository.getAuthenticatedAuthor();
        Author followed = authorRepository.findBySlugAndIsDisabledFalse(slug).orElseThrow(()->new AuthorNotFoundException(slug));
        Optional<FollowRelation> followRelation = authorRelationRepository.findFollowRelationBetween(follower.getId(), followed.getId());
        if(followRelation.isPresent()){
            throw new AuthorAlreadyFollowedException(followed.getSlug());
        }
        Optional<FollowRequest> followRequestOpt = followRequestCollection.findByFollowedIdAndFollowerId(followed.getId(), follower.getId());
        if(followRequestOpt.isPresent()){
            throw new BadRequestException("Follow request is already sent");
        }
        FollowRequest followRequest = followed.requestToFollow(follower.getId(), UuidCreator.getTimeOrdered());
        followRequestCollection.save(followRequest);
    }

    @Transactional
    public void follow(String slug) {
        Author follower = authorRepository.getAuthenticatedAuthor();
        Author followed = authorRepository.findBySlugAndIsDisabledFalseAndNotBeingBlocked(slug).orElseThrow(()->new AuthorNotFoundException(slug));
        if(followed.getIsPrivate()){
            throw new UnauthorizedException("User can't be follow directly without a follow request");
        }
        Optional<FollowRelation> followRelation = authorRelationRepository.findFollowRelationBetween(follower.getId(), followed.getId());
        if(followRelation.isPresent()){
            throw new AuthorAlreadyFollowedException(followed.getId());
        }
        followed.follow(follower);
        StaticEventPublisher.publishEvent(new AuthorFollowedEvent(followed, follower));
        authorRelationRepository.save(new FollowRelation(follower.getId(), followed.getId()));
        authorRepository.save(follower);
        authorRepository.save(followed);
    }

    @Transactional
    public void unfollow(String slug) {
        Author follower = authorRepository.getAuthenticatedAuthor();
        Author followed = authorRepository.findBySlugAndIsDisabledFalse(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        Optional<FollowRelation> followRelation = authorRelationRepository.findFollowRelationBetween(follower.getId(), followed.getId());
        if(followRelation.isEmpty()){
            throw new AuthorNotFollowedException(followed.getId());
        }
        followed.unfollow(follower);
        authorRelationRepository.delete(followRelation.get());
        authorRepository.saveAll(List.of(follower, followed));
    }

    @Transactional
    public void removeFollower(String slug) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Author removedFollower = authorRepository.findBySlugAndIsDisabledFalse(slug).orElseThrow(()->new AuthorNotFoundException(slug));
        Optional<FollowRelation> followRelation = authorRelationRepository.findFollowRelationBetween(removedFollower.getId(), author.getId());
        if(followRelation.isEmpty()){
            throw new AuthorNotFollowedException(removedFollower.getId());
        }
        author.unfollow(removedFollower);
        authorRelationRepository.delete(followRelation.get());
        authorRepository.saveAll(List.of(author, removedFollower));
    }
    @Transactional
    public void block(String slug) {
        Author blocker = authorRepository.getAuthenticatedAuthor();
        Author blocked = authorRepository.findBySlugAndIsDisabledFalse(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        Optional<BlockRelation> blockRelation = authorRelationRepository.findBlockRelationBetween(blocker.getId(), blocked.getId());
        if(blockRelation.isPresent()){
            throw new AuthorAlreadyBlocked(blocked.getId());
        }
        authorRelationRepository.save(new BlockRelation(blocker.getId(), blocked.getId()));
    }

    @Transactional
    public void unblock(String slug) {
        Author blocker = authorRepository.getAuthenticatedAuthor();
        Author blocked = authorRepository.findBySlugAndIsDisabledFalse(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        Optional<BlockRelation> blockRelation = authorRelationRepository.findBlockRelationBetween(blocker.getId(), blocked.getId());
        if(blockRelation.isEmpty()){
            throw new AuthorNotBlockedException(blocked.getSlug());
        }
        authorRelationRepository.delete(blockRelation.get());
    }

    @Transactional
    public void updateName(String name) {
        Author author = authorRepository.getAuthenticatedAuthor();
        author.updateName(name);
        authorRepository.save(author);
    }
    @Transactional
    public void updateSlug(String slug) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Optional<Author> checkAuthorWithSameSlug = authorRepository.findBySlugAndIsDisabledFalse(slug);
        if(checkAuthorWithSameSlug.isPresent()){
            throw new SlugAlreadyTakenException(slug);
        }
        author.updateSlug(slug.trim().toLowerCase(Locale.ENGLISH));
        authorRepository.save(author);
    }
    @Transactional
    public void updateBio(String bio) {
        Author author = authorRepository.getAuthenticatedAuthor();
        author.updateBio(bio);
        authorRepository.save(author);
    }
    @Transactional
    public Author updateProfileImage(MultipartFile image) throws IOException {
        String avatarFileName = imageService.uploadProfileImage(image);
        Author author = authorRepository.getAuthenticatedAuthor();
        if (author.getAvatarFileName() != null) {
            imageService.deleteFile(author.getAvatarFileName());
        }
        author.attachFileName(avatarFileName);
        authorRepository.save(author);
        return author;
    }
    @Transactional
    public void removeMyProfileImage() {
        Author author = authorRepository.getAuthenticatedAuthor();
        if (author.getAvatarFileName() != null) {
            imageService.deleteFile(author.getAvatarFileName());
            author.removeProfilePhoto();
            authorRepository.save(author);
        }
    }

    @Transactional
    public void acceptFollowRequest(String followerSlug) {
        Author followed = authorRepository.getAuthenticatedAuthor();
        Author follower = authorRepository.findBySlugAndIsDisabledFalseAndNotBeingBlocked(followerSlug).orElseThrow(()-> new AuthorNotFoundException(followerSlug));
        FollowRequest followRequest = followRequestCollection.findByFollowedIdAndFollowerId(followed.getId(), follower.getId()).orElseThrow(FollowRequestNotFoundException::new);
        followRequest.accept();
        Optional<FollowRelation> followRelation = authorRelationRepository.findFollowRelationBetween(follower.getId(), followed.getId());
        if(followRelation.isPresent()){
            throw new AuthorAlreadyFollowedException(followed.getId());
        }
        followed.follow(follower);
        authorRelationRepository.save(new FollowRelation(follower.getId(), followed.getId()));
        authorRepository.saveAll(List.of(follower, followed));
        followRequestCollection.delete(followRequest);
    }

    @Transactional
    public void deleteFollowRequest(String followedAuthorSlug, String followerAuthorSlug) {
        Author follower = authorRepository.findBySlugAndIsDisabledFalseAndNotBeingBlocked(followerAuthorSlug).orElseThrow(()-> new AuthorNotFoundException(followedAuthorSlug));
        Author followed = authorRepository.findBySlugAndIsDisabledFalseAndNotBeingBlocked(followedAuthorSlug).orElseThrow(()-> new AuthorNotFoundException(followedAuthorSlug));;
        Author performer = authorRepository.getAuthenticatedAuthor();
        if(!(performer.getSlug().equals(follower.getSlug()) || performer.getSlug().equals(followed.getSlug()))){
            throw new UnauthorizedException();
        }
        FollowRequest followRequest = followRequestCollection.findByFollowedIdAndFollowerId(followed.getId(), follower.getId()).orElseThrow(FollowRequestNotFoundException::new);
        followRequestCollection.delete(followRequest);
    }
}
