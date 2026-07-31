package com.rbac.admin.repository;

import com.rbac.admin.entity.JobConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobConfigRepository extends JpaRepository<JobConfig, Long> {
}
