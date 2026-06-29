package com.zima.zimasocial.context.social.author.application;

import com.zima.zimasocial.context.social.author.abstracted.AuthorUseCase;
import com.zima.zimasocial.context.social.author.exception.*;
import com.zima.zimasocial.context.social.author.event.AuthorFollowedEvent;
import com.zima.zimasocial.context.social.author.event.AuthorFollowRequestAcceptedEvent;
import com.zima.zimasocial.context.social.image.ImageService;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.entity.AuthorRelation;
import com.zima.zimasocial.context.social.author.entity.FollowRequest;
import com.zima.zimasocial.context.social.author.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.repository.FollowRequestRepository;
import com.zima.zimasocial.context.social.author.value.Relation;
import com.zima.zimasocial.shared.exception.ConflictException;
import com.zima.zimasocial.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorApplicationService implements AuthorUseCase {
    private final AuthorRepository authorRepository;
    private final AuthorRelationRepository authorRelationRepository;
    private final FollowRequestRepository followRequestRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ImageService imageService;
    @Override
    public void requestToFollow(String slug) {
        Author follower = authorRepository.getAuthenticatedAuthor();
        Author followed = authorRepository.findBySlug(slug).orElseThrow(AuthorNotFoundException::new);
        Optional<AuthorRelation> followRelation = authorRelationRepository.findByActorAndReceiverAndRelation(follower, followed, Relation.followed);
        if(followRelation.isPresent()){
            throw new AuthorAlreadyFollowedException(followed.getSlug());
        }
        Optional<FollowRequest> followRequestOpt = followRequestRepository.findByFollowerIdAndFollowedId(follower.getId(), followed.getId());
        if(followRequestOpt.isPresent()){
            throw new ConflictException("Follow request is already sent");
        }
        FollowRequest followRequest = followed.requestToFollow(follower.getId());
        follower.incrementFollowingCount();
        followRequestRepository.save(followRequest);
    }

    @Override
    @Transactional
    public void follow(String slug) {
        Author follower = authorRepository.getAuthenticatedAuthor();
        Author followed = authorRepository.findBySlug(slug).orElseThrow(()->new AuthorNotFoundException(slug));
        if(followed.getIsPrivate()){
            throw new UnauthorizedException("User can't be follow directly without a follow request");
        }
        Optional<AuthorRelation> followRelation = authorRelationRepository.findByActorAndReceiverAndRelation(follower, followed, Relation.followed);
        if(followRelation.isPresent()){
            throw new AuthorAlreadyFollowedException(followed.getSlug());
        }
        AuthorRelation authorRelation = followed.follow(follower.getId());
        follower.incrementFollowingCount();
        authorRelationRepository.save(authorRelation);
        authorRepository.saveAll(List.of(follower, followed));
        applicationEventPublisher.publishEvent(new AuthorFollowedEvent(followed, follower));
    }

    @Override
    public void unfollow(String slug) {
        Author unfollower = authorRepository.getAuthenticatedAuthor();
        Author followed = authorRepository.findBySlug(slug).orElseThrow(AuthorNotFoundException::new);
        Optional<AuthorRelation> followRelation = authorRelationRepository.findByActorAndReceiverAndRelation(unfollower, followed, Relation.followed);
        if(followRelation.isEmpty()){
            throw new AuthorNotFollowedException(followed.getSlug());
        }
        followed.unfollow(unfollower.getId());
        unfollower.decrementFollowingCount();
        authorRelationRepository.delete(followRelation.get());
        authorRepository.saveAll(List.of(unfollower, followed));
    }

    @Override
    public void removeFollower(String slug) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Author removedFollower = authorRepository.findBySlug(slug).orElseThrow(()->new AuthorNotFoundException(slug));
        Optional<AuthorRelation> followRelation = authorRelationRepository.findByActorAndReceiverAndRelation(removedFollower, author, Relation.followed);
        if(followRelation.isEmpty()){
            throw new AuthorNotFollowedException(removedFollower.getSlug());
        }
        removedFollower.unfollow(author.getId());
        author.decrementFollowerCount();
        authorRelationRepository.delete(followRelation.get());
        authorRepository.saveAll(List.of(author, removedFollower));
    }

    @Override
    public void block(String slug) {
        Author blocker = authorRepository.getAuthenticatedAuthor();
        Author blocked = authorRepository.findBySlug(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        Optional<AuthorRelation> blockRelationOpt = authorRelationRepository.findByActorAndReceiverAndRelation(blocker, blocked, Relation.blocked);
        if(blockRelationOpt.isPresent()){
            throw new AuthorAlreadyBlocked(blocked.getSlug());
        }
        AuthorRelation blockRelation = blocked.block(blocker.getId());
        authorRelationRepository.save(blockRelation);
    }

    @Override
    public void unblock(String slug) {
        Author blocker = authorRepository.getAuthenticatedAuthor();
        Author blocked = authorRepository.findBySlug(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        Optional<AuthorRelation> blockRelation = authorRelationRepository.findByActorAndReceiverAndRelation(blocker, blocked, Relation.blocked);
        if(blockRelation.isEmpty()){
            throw new AuthorNotBlockedException(blocked.getSlug());
        }
        authorRelationRepository.delete(blockRelation.get());

    }

    @Override
    public void updateName(String name) {
        Author author = authorRepository.getAuthenticatedAuthor();
        author.updateName(name);
        authorRepository.save(author);
    }

    @Transactional
    public void updateSlug(String slug) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Optional<Author> checkAuthorWithSameSlug = authorRepository.findBySlug(slug);
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
        Author follower = authorRepository.findBySlug(followerSlug).orElseThrow(()-> new AuthorNotFoundException(followerSlug));
        FollowRequest followRequest = followRequestRepository.findByFollowerIdAndFollowedId(follower.getId(), followed.getId()).orElseThrow(FollowRequestNotFoundException::new);
        followRequest.accept();
        Optional<AuthorRelation> followRelationOpt = authorRelationRepository.findByActorAndReceiverAndRelation(follower, followed, Relation.followed);
        if(followRelationOpt.isPresent()){
            throw new AuthorAlreadyFollowedException(followed.getSlug());
        }
        AuthorRelation followRelation = followed.follow(follower.getId());
        authorRelationRepository.save(followRelation);
        authorRepository.saveAll(List.of(follower, followed));
        followRequestRepository.delete(followRequest);
        applicationEventPublisher.publishEvent(new AuthorFollowRequestAcceptedEvent(follower.getId(), followed.getId()));
    }

    @Transactional
    public void deleteFollowRequest(String followedAuthorSlug, String followerAuthorSlug) {
        Author follower = authorRepository.findBySlug(followerAuthorSlug).orElseThrow(()-> new AuthorNotFoundException(followedAuthorSlug));
        Author followed = authorRepository.findBySlug(followedAuthorSlug).orElseThrow(()-> new AuthorNotFoundException(followedAuthorSlug));;
        Author performer = authorRepository.getAuthenticatedAuthor();
        if(!(performer.getSlug().equals(follower.getSlug()) || performer.getSlug().equals(followed.getSlug()))){
            throw new UnauthorizedException();
        }
        FollowRequest followRequest = followRequestRepository.findByFollowerIdAndFollowedId(follower.getId(), followed.getId()).orElseThrow(FollowRequestNotFoundException::new);
        followRequestRepository.delete(followRequest);
    }
}
