package com.zimaberlin.zimasocial.context.social.infastructure.jpaentities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "media_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MediaItem {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "resource_url")
    private String resourceUrl;

    @Column(name = "provider")
    private String provider;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content", columnDefinition = "jsonb")
    private String content;
}
