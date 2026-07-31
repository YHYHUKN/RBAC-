package com.rbac.admin.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "sys_announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    /** 通知 / 公告 */
    @Column(length = 20)
    private String type = "通知";

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 50)
    private String publisher;

    /** 0=草稿 1=已发布 */
    private Integer status = 0;

    /** 1=普通 2=重要 3=紧急 */
    private Integer priority = 1;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    private LocalDateTime publishTime;
}
