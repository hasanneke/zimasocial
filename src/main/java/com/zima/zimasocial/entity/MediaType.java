package com.zima.zimasocial.entity;

import lombok.Getter;

@Getter
public enum MediaType {
    music("spotify", "Müzik"),
    movie("tdmb", "Film"),
    tv("tdmb", "Dizi"),
    book("google", "Kitap"),
    any("","");
    String provider;
    String title;
    MediaType(String provider, String title){
    this.provider = provider;
    this.title = title;
} }
