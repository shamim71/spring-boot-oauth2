package com.myappteam.microservice.auth.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@SuppressWarnings("serial")
@Entity
@Table(name = "customer_info",uniqueConstraints={@UniqueConstraint(columnNames={"domain"})})
public class CustomerInfo extends AbstractEntity{

	/** Customer domain. */
	@Column(name = "domain", length = 255)
	private String domain;
	
	/** Customer code. */
	@Column(name = "code", length = 255)
	private String code;
	
	/** Customer name. */
	@Column(name = "name", length = 255)
	private String name;
	
	/** Customer phone. */
	@Column(name = "phone", length = 50)
	private String phone;
	
	@Column(name = "company_size")
	private int companySize;
	
	@Column(name = "active")
	private Boolean isActive;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCompanySize() {
		return companySize;
	}

	public void setCompanySize(int companySize) {
		this.companySize = companySize;
	}

	
}
