package com.rbac.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId = 0L;

    @Column(nullable = false)
    private String name;

    private Integer sort = 0;

    @CreationTimestamp
    private LocalDateTime createTime;
}
