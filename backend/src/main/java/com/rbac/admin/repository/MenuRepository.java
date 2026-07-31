package com.rbac.admin.repository;

import com.rbac.admin.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByParentIdOrderBySortAsc(Long parentId);
    List<Menu> findAllByOrderBySortAsc();
}
