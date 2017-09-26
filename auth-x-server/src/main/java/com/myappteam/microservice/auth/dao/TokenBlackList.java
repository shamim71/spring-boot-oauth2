package com.myappteam.microservice.auth.dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TokenBlackList {
	@Id
	private String jti;
	private Long userId;
	private Long expires;
	private boolean isBlackListed;

	public TokenBlackList() {
	}

	public TokenBlackList(Long userId, String jti, Long expires) {
		this.jti = jti;
		this.userId = userId;
		this.expires = expires;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getExpires() {
		return expires;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}

	public boolean isBlackListed() {
		return isBlackListed;
	}

	public void setBlackListed(boolean isBlackListed) {
		this.isBlackListed = isBlackListed;
	}



	
}