package com.zimaberlin.zimasocial.entity.file;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Entity
@Table(name = "file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction(value = "IS_DELETED IS FALSE")
public class FileEntity  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "NAME", nullable = false, unique = true, length = 2048)
    private String name;

    @Column(name = "MEDIA_TYPE", nullable = false)
    private String mediaType;

    @Column(name = "DATA")
    private byte[] data;

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "CREATED_AT", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "FILE_TYPE")
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(name = "CODE")
    @Enumerated(EnumType.STRING)
    private FileCode code;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
