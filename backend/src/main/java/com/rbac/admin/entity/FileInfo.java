package com.rbac.admin.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "sys_file")
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String storageName;

    @Column(nullable = false)
    private String path;

    private Long size;

    @Column(length = 20)
    private String ext;

    @Column(length = 50)
    private String contentType;

    private String uploader;

    @CreationTimestamp
    private LocalDateTime createTime;
}
