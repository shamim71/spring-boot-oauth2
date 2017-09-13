package com.myappteam.microservice.auth.dao;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Entity for customer data.
 * 
 * @author Shamim Ahmmed
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "role")
public class Role extends AbstractEntity {

	private String name;

	private String code;

	@Column(name="default_home_page_id")
	private Integer defaultHomePageId;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name = "role_rights", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "right_id", referencedColumnName = "id"))
	private Set<Right> rights;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<Right> getRights() {
		return rights;
	}

	public void setRights(Set<Right> rights) {
		this.rights = rights;
	}

	public Integer getDefaultHomePageId() {
		return defaultHomePageId;
	}

	public void setDefaultHomePageId(Integer defaultHomePageId) {
		this.defaultHomePageId = defaultHomePageId;
	}

}
