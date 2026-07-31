package com.rbac.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "roles")
@Entity
@Table(name = "sys_menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 父级菜单ID，顶级为 0 */
    private Long parentId = 0L;

    /** 菜单名称 */
    @Column(nullable = false)
    private String name;

    /** 菜单类型：0 目录 1 菜单 2 按钮 */
    private Integer type = 1;

    /** 路由地址 */
    private String path;

    /** 前端组件路径 */
    private String component;

    /** 图标 */
    private String icon;

    /** 权限标识 */
    private String permission;

    private Integer sort = 0;

    @ManyToMany(mappedBy = "menus", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Role> roles;

    @CreationTimestamp
    private LocalDateTime createTime;
}
