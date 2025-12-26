package com.zimaberlin.zimasocial.entity;

import lombok.Getter;

@Getter
public enum PostType {
    music("spotify"),
    movie("tdmb"),
    book("google"),
    any("");
    String provider;
    PostType(String provider){
    this.provider = provider;
} }
