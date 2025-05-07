package com.zimaberlin.zimasocial.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;
}
