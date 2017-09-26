package com.myappteam.microservice.auth.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



@SuppressWarnings("serial")
@Entity
@Table(name = "user_info",uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class UserInfo extends AbstractEntity {

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(length = 255)
	private String salt;

	@Column(name = "hashed_password", length = 255)
	private String hashedPassword;

	/** Employee phone. */
	@Column(name = "phone", length = 255)
	private String phone;

	/** Employee role. */
	@OneToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@Column(name = "qbase_employee_ref")
	private String qBaseEmployeeRef;

	@Column(name = "recent_login")
	private Date recentLogin;

	@Column(name = "active")
	private Boolean isActive;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired; 

    
    public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public UserInfo() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;

    }
    
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getqBaseEmployeeRef() {
		return qBaseEmployeeRef;
	}

	public void setqBaseEmployeeRef(String qBaseEmployeeRef) {
		this.qBaseEmployeeRef = qBaseEmployeeRef;
	}

	public Date getRecentLogin() {
		return recentLogin;
	}

	public void setRecentLogin(Date recentLogin) {
		this.recentLogin = recentLogin;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
