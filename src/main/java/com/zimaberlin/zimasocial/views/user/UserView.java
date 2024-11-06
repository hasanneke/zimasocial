package com.zimaberlin.zimasocial.views.user;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserView {
    private String slug;
    private String name;
    private String familyName;
    private String avatarUrl;
    private String bio;
}
