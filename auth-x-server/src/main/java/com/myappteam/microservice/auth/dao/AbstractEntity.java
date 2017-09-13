package com.myappteam.microservice.auth.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract superclass for all Entity-classes, containing ID, creation date and
 * last modification date.
 * 
 * @author Shamim Ahmmed
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Long getRecordOwner() {
		return recordOwner;
	}

	public void setRecordOwner(Long recordOwner) {
		this.recordOwner = recordOwner;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Column(name = "date_created")
	protected Date dateCreated;

	@Column(name = "date_modified")
	protected Date dateModified;

	@Column(name = "record_owner")
	protected Long recordOwner;
	
	@Column(name = "last_modified_by")
	protected Long lastModifiedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	
}
