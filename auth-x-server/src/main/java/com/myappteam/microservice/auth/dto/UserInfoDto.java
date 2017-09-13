package com.myappteam.microservice.auth.dto;

import java.util.ArrayList;
import java.util.List;

import com.myappteam.microservice.auth.dao.Right;
import com.myappteam.microservice.auth.dao.UserInfo;


public class UserInfoDto {

	private String firstName;

	private String lastName;

	private String email;

	private String role;


	private String qBaseRef;
	
	private List<String> permissions;
	
	
	public UserInfoDto() {
		super();
	}
	
	public UserInfoDto(UserInfo user) {
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.role = user.getRole().getCode();
		this.qBaseRef = user.getqBaseEmployeeRef();
		this.permissions = new ArrayList<String>();
		
		if(user.getRole().getRights() != null && user.getRole().getRights().size() >0){
			for(Right r: user.getRole().getRights()){
				this.getPermissions().add(r.getCode());
			}
		}
	}
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getqBaseRef() {
		return qBaseRef;
	}

	public void setqBaseRef(String qBaseRef) {
		this.qBaseRef = qBaseRef;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
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

}
