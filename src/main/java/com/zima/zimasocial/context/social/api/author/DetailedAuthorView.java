package com.zima.zimasocial.context.social.api.author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DetailedAuthorView extends AuthorView {
    private String email;
    public void mergeAuthorView(AuthorView authorView) {
        setId(authorView.getId());
        setSlug(authorView.getSlug());
        setName(authorView.getName());
        setFamilyName(authorView.getFamilyName());
        setAvatarUrl(authorView.getAvatarUrl());
        setBio(authorView.getBio());
        setFollowerCount(authorView.getFollowerCount());
        setFollowingCount(authorView.getFollowingCount());
        setFollowed(authorView.isFollowed());
        setIsPrivate(authorView.getIsPrivate());
        setIsBlocked(authorView.getIsBlocked());
        setTermsOfUseAccepted(authorView.getTermsOfUseAccepted());
    }
}
