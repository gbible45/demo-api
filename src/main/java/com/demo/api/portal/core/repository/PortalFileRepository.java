package com.demo.api.portal.core.repository;

import com.demo.api.portal.core.domain.PortalFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortalFileRepository extends JpaRepository <PortalFile, Long> {
    List<PortalFile> findByTableName(String tableName);
    Page<PortalFile> findByTableName(String tableName, Pageable pageable);
    List<PortalFile> findByTableNameAndParentId(String tableName, long parentId);
    Page<PortalFile> findByTableNameAndParentId(String tableName, long parentId, Pageable pageable);
    List<PortalFile> findByTableNameAndParentIdAndFieldName(String tableName, long parentId, String fieldName);
    Page<PortalFile> findByTableNameAndParentIdAndFieldName(String tableName, long parentId, String fieldName, Pageable pageable);
    PortalFile getByTableNameAndParentIdAndFieldName(String tableName, long parentId, String fieldName);
    PortalFile findById(long id);
    void deleteById(long id);
}
