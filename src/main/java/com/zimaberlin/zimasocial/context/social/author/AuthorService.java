package com.zimaberlin.zimasocial.context.social.author;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zimaberlin.zimasocial.context.social.authorrelation.*;
import com.zimaberlin.zimasocial.context.social.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        FollowRequest followRequest = followed.requestToFollow(follower.getId(), UuidCreator.getTimeOrdered());
        followRequestCollection.save(followRequest);
    }

    @Transactional
    public void follow(String slug) {
        Author follower = authorRepository.getAuthenticatedAuthor();
        Author followed = authorRepository.findBySlugAndIsDisabledFalse(slug).orElseThrow(()->new AuthorNotFoundException(slug));
        Optional<FollowRelation> followRelation = authorRelationRepository.findFollowRelationBetween(follower.getId(), followed.getId());
        if(followRelation.isPresent()){
            throw new AuthorAlreadyFollowedException(followed.getId());
        }
        followed.follow(follower);
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
            throw new AuthorNotFollowed(followed.getId());
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
            throw new AuthorNotFollowed(removedFollower.getId());
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
            throw new AuthorNotBlockedException(blocked.getId().getId());
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
        author.updateSlug(slug);
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

    public void acceptFollowRequest(UUID id) {
        FollowRequest followRequest = followRequestCollection.findById(id).orElseThrow(FollowRequestNotFoundException::new);
        followRequest.accept();
        followRequestCollection.save(followRequest);
    }

    public void deleteFollowRequest(UUID id) {
        FollowRequest followRequest = followRequestCollection.findById(id).orElseThrow(FollowRequestNotFoundException::new);
        followRequestCollection.delete(followRequest);
    }
}
