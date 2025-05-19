package com.zimaberlin.zimasocial.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;
}
