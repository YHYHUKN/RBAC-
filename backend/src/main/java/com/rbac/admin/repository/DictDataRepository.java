package com.rbac.admin.repository;

import com.rbac.admin.entity.DictData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DictDataRepository extends JpaRepository<DictData, Long>, JpaSpecificationExecutor<DictData> {
    List<DictData> findByTypeOrderBySortAsc(String type);

    List<DictData> findByType(String type);
}
