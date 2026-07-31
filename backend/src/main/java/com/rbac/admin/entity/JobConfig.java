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
@Table(name = "sys_job")
public class JobConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String jobName;

    @Column(nullable = false, length = 100)
    private String jobGroup = "DEFAULT";

    @Column(nullable = false, length = 100)
    private String cron;

    @Column(length = 200)
    private String description;

    /** 0=暂停 1=运行中 */
    private Integer status = 1;

    @Column(length = 100)
    private String beanName = "sampleJob";

    @CreationTimestamp
    private LocalDateTime createTime;
}
