package com.rbac.admin.repository;

import com.rbac.admin.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long>, JpaSpecificationExecutor<FileInfo> {
}
