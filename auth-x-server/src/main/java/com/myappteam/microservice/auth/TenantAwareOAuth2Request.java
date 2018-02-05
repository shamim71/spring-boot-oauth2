package com.myappteam.microservice.auth;

import org.springframework.security.oauth2.provider.OAuth2Request;

public class TenantAwareOAuth2Request extends OAuth2Request {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TenantAwareOAuth2Request(OAuth2Request other) {
        super(other);
    }
 
    private String tenant;
 
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
 
    public String getTenant() {
        return tenant;
    }
}
