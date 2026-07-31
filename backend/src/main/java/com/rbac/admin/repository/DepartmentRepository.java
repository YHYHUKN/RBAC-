package com.rbac.admin.repository;

import com.rbac.admin.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByParentIdOrderBySortAsc(Long parentId);
}
