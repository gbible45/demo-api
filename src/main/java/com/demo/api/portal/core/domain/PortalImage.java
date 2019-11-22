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
public class PortalImage implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false, length = 50)
	private String contentType;

	@Column(nullable = false)
	private String imageName;

	private long fileSize;

	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(nullable = false)
	private byte[] image;

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

	public PortalImage(String contentType, String imageName, byte[] image, long fileSize) {
		this.contentType = contentType;
		this.imageName = imageName;
		this.image = image;
		this.fileSize = fileSize;
	}

	public PortalImage(String contentType, String imageName, byte[] image, long fileSize, String tableName, String fieldName) {
		this.contentType = contentType;
		this.imageName = imageName;
		this.image = image;
		this.fileSize = fileSize;
		this.tableName = tableName;
		this.fieldName = fieldName;
	}

	public PortalImage(String contentType, String imageName, byte[]image, long fileSize, String tableName, String fieldName, long parentId) {
		this.contentType = contentType;
		this.imageName = imageName;
		this.image = image;
		this.fileSize = fileSize;
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.parentId = parentId;
	}
}
