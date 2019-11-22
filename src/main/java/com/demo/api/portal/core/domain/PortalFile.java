package com.demo.api.portal.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PortalFile implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	private String target;

	@Column(nullable = false)
	private String subPath;

	@Column(nullable = false)
	private String contentType;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String originalFileName;

	private long fileSize;

	private String filePath;

	private String downloadUrl;

	@Column(length = 50)
	private String tableName;

	@Column(length = 50)
	private String fieldName;

	private long parentId;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updated;

	public PortalFile(String target, String subPath, String contentType, String fileName, String originalFileName, long fileSize) {
		this.target = target;
		this.subPath = subPath;
		this.contentType = contentType;
		this.fileName = fileName;
		this.originalFileName = originalFileName;
		this.fileSize = fileSize;
	}

	public PortalFile(String target, String subPath, String contentType, String fileName, String originalFileName, long fileSize, String filePath, String downloadUrl, String tableName, String fieldName) {
		this.target = target;
		this.subPath = subPath;
		this.contentType = contentType;
		this.fileName = fileName;
		this.originalFileName = originalFileName;
		this.fileSize = fileSize;
		this.filePath = filePath;
		this.downloadUrl = downloadUrl;
		this.tableName = tableName;
		this.fieldName = fieldName;
	}

	public PortalFile(String target, String subPath, String contentType, String fileName, String originalFileName, long fileSize, String filePath, String downloadUrl, String tableName, String fieldName, long parentId) {
		this.target = target;
		this.subPath = subPath;
		this.contentType = contentType;
		this.fileName = fileName;
		this.originalFileName = originalFileName;
		this.fileSize = fileSize;
		this.filePath = filePath;
		this.downloadUrl = downloadUrl;
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.parentId = parentId;
	}

}
