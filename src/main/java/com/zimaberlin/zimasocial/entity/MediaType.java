package com.zimaberlin.zimasocial.entity;

import lombok.Getter;

@Getter
public enum MediaType {
    music("spotify"),
    movie("tdmb"),
    tv("tdmb"),
    book("google"),
    any("");
    String provider;
    MediaType(String provider){
    this.provider = provider;
} }
