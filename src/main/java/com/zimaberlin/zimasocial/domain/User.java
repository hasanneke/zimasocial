package com.zimaberlin.zimasocial.domain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String slug;
    private String name;
    private String familyName;
    private String avatarUrl;
}
