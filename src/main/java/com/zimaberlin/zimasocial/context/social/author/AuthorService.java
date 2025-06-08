package com.zimaberlin.zimasocial.context.social.author;

import com.zimaberlin.zimasocial.context.social.image.ImageService;
import com.zimaberlin.zimasocial.context.social.authorrelation.BlockRelation;
import com.zimaberlin.zimasocial.context.social.authorrelation.FollowRelation;
import com.zimaberlin.zimasocial.context.social.authorrelation.AuthorRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;
    private AuthorRelationRepository authorRelationRepository;
    private ImageService imageService;
    @Autowired
    public AuthorService(AuthorRepository authorRepository, AuthorRelationRepository authorRelationRepository, ImageService imageService) {
        this.authorRepository = authorRepository;
        this.authorRelationRepository = authorRelationRepository;
        this.imageService = imageService;
    }

    @Transactional
    public void follow(String slug) {
        Author follower = authorRepository.getAuthenticatedAuthor();
        Author followed = authorRepository.findBySlug(slug).orElseThrow(()->new AuthorNotFoundException(slug));
        Optional<FollowRelation> followRelation = authorRelationRepository.findFollowRelationBetween(follower.getAuthorId(), followed.getAuthorId());
        if(followRelation.isPresent()){
            throw new AuthorAlreadyFollowedException(followed.getAuthorId());
        }
        followed.follow(follower);
        authorRelationRepository.save(new FollowRelation(follower.getAuthorId(), followed.getAuthorId()));
    }

    @Transactional
    public void unfollow(String slug) {
        Author follower = authorRepository.getAuthenticatedAuthor();
        Author followed = authorRepository.findBySlug(slug).orElseThrow(()->new AuthorNotFoundException(slug));
        Optional<FollowRelation> followRelation = authorRelationRepository.findFollowRelationBetween(follower.getAuthorId(), followed.getAuthorId());
        if(followRelation.isEmpty()){
            throw new AuthorNotFollowed(followed.getAuthorId());
        }
        followed.unfollow(follower);
        authorRelationRepository.delete(followRelation.get());
    }
    @Transactional
    public void block(String slug) {
        Author blocker = authorRepository.getAuthenticatedAuthor();
        Author blocked = authorRepository.findBySlug(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        Optional<BlockRelation> blockRelation = authorRelationRepository.findBlockRelationBetween(blocker.getAuthorId(), blocked.getAuthorId());
        if(blockRelation.isPresent()){
            throw new AuthorAlreadyBlocked(blocked.getAuthorId());
        }
        authorRelationRepository.save(new BlockRelation(blocker.getAuthorId(), blocked.getAuthorId()));
    }

    @Transactional
    public void unblock(String slug) {
        Author blocker = authorRepository.getAuthenticatedAuthor();
        Author blocked = authorRepository.findBySlug(slug).orElseThrow(()-> new AuthorNotFoundException(slug));
        Optional<BlockRelation> blockRelation = authorRelationRepository.findBlockRelationBetween(blocker.getAuthorId(), blocked.getAuthorId());
        if(blockRelation.isEmpty()){
            throw new AuthorNotBlockedException(blocked.getAuthorId());
        }
        authorRelationRepository.save(blockRelation.get());
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
        Optional<Author> checkAuthorWithSameSlug = authorRepository.findBySlug(slug);
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
    public void updateProfileImage(MultipartFile image) throws IOException {
        String avatarFileName = imageService.uploadProfileImage(image);
        Author author = authorRepository.getAuthenticatedAuthor();
        if (author.getAvatarFileName() != null) {
            imageService.deleteFile(author.getAvatarFileName());
        }
        author.attachFileName(avatarFileName);
        authorRepository.save(author);
    }

    public void removeMyProfileImage() {
        Author author = authorRepository.getAuthenticatedAuthor();
        if (author.getAvatarFileName() != null) {
            imageService.deleteFile(author.getAvatarFileName());
            author.removeProfilePhoto();
            authorRepository.save(author);
        }
    }
}
