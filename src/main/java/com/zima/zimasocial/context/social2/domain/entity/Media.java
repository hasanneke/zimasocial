package com.zima.zimasocial.context.social2.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zima.zimasocial.context.social2.domain.value.MediaId;
import com.zima.zimasocial.entity.MediaType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "media")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Media {
    @EmbeddedId
    @GeneratedValue
    @UuidGenerator
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", updatable = false)
    )
    private MediaId id;

    @Column(name = "resource_id")
    @JsonIgnore
    private String resourceId;

    @Column(name = "resource_url")
    @JsonIgnore
    private String resourceUrl;

    @Column(name = "provider")
    private String provider;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content", columnDefinition = "jsonb")
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MediaType type;
}
