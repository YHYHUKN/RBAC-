package com.rbac.admin.repository;

import com.rbac.admin.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    List<OperationLog> findAllByOrderByCreateTimeDesc();
}
