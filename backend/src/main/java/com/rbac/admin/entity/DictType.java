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
@Table(name = "sys_dict_type")
public class DictType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String type;

    @Column(nullable = false, length = 50)
    private String name;

    private Integer status = 1;

    private Integer sort = 0;

    @Column(length = 200)
    private String remark;

    @CreationTimestamp
    private LocalDateTime createTime;
}
