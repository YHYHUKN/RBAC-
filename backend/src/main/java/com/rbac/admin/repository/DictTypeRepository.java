package com.rbac.admin.repository;

import com.rbac.admin.entity.DictType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DictTypeRepository extends JpaRepository<DictType, Long>, JpaSpecificationExecutor<DictType> {
    boolean existsByType(String type);
    DictType findByType(String type);
}
