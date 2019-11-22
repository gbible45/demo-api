package com.demo.api.portal.core.repository;

import com.demo.api.portal.core.domain.PortalImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortalImageRepository extends JpaRepository <PortalImage, Long> {
    List<PortalImage> findByTableName(String tableName);
    Page<PortalImage> findByTableName(String tableName, Pageable pageable);
    List<PortalImage> findByTableNameAndParentId(String tableName, long parentId);
    Page<PortalImage> findByTableNameAndParentId(String tableName, long parentId, Pageable pageable);
    List<PortalImage> findByTableNameAndParentIdAndFieldName(String tableName, long parentId, String fieldName);
    Page<PortalImage> findByTableNameAndParentIdAndFieldName(String tableName, long parentId, String fieldName, Pageable pageable);
    PortalImage getByTableNameAndParentIdAndFieldName(String tableName, long parentId, String fieldName);
    PortalImage findById(long id);
    void deleteById(long id);
}
