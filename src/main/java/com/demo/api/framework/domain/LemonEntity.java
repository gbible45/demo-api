package com.demo.api.framework.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Base class for all entities.
 * 
 * @author Sanjay Patel
 *
 * @param <U>
 * @param <ID>
 */
@MappedSuperclass
//@JsonIgnoreProperties({ "createdBy", "lastModifiedBy" })
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class LemonEntity<U extends AbstractUser<U,ID>, ID extends Serializable> extends AbstractPersistable<ID> { //>extends JapAbstractAuditable<U, ID> {

	private static final long serialVersionUID = -8151190931948396443L;

/*
	@ManyToOne
	@CreatedBy
	private U createdBy;
*/

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdDate;

/*
	@ManyToOne
	@LastModifiedBy
	private U lastModifiedBy;
*/

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date lastModifiedDate;

	/**
	 * Whether the given user has the given permission for
	 * this entity. Override this method where you need.
	 * 
	 * @param user
	 * @param permission
	 * @return
	 */
	public boolean hasPermission(U user, String permission) {
		return false;
	}

/*
	public U getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(U createdBy) {
		this.createdBy = createdBy;
	}
*/

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

/*
	public U getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(U lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
*/

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
