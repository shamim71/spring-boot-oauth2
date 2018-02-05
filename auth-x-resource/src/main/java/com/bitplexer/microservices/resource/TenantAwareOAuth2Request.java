package com.bitplexer.microservices.resource;

import org.springframework.security.oauth2.provider.OAuth2Request;

@SuppressWarnings("serial")
public class TenantAwareOAuth2Request extends OAuth2Request {
	
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
