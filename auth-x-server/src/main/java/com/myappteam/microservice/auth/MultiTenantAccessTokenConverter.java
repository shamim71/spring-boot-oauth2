package com.myappteam.microservice.auth;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.myappteam.microservice.auth.dto.CustomUserPrincipal;

public class MultiTenantAccessTokenConverter extends JwtAccessTokenConverter{

	private static final Logger logger = LoggerFactory.getLogger(MultiTenantAccessTokenConverter.class);
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		String tenantId = getTenantIdentifier(authentication);
	    
		Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());

		logger.debug("Enhancing default token with tenant Id: "+ tenantId);
		
		info.put("tenant_id", tenantId);
	    DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
	    
	    customAccessToken.setAdditionalInformation(info);

		return super.enhance(customAccessToken, authentication);
	}

	private String getTenantIdentifier(OAuth2Authentication authentication) {
		 String tenant = null;
		if(authentication.getPrincipal() instanceof CustomUserPrincipal){
			CustomUserPrincipal p = (CustomUserPrincipal) authentication.getPrincipal();
			tenant = p.getTenantId();
		}
	    return tenant;
	}


	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		logger.debug("extractAuthentication  token with tenant Id: ");
		
		 OAuth2Authentication authentication = super.extractAuthentication(map);
		 TenantAwareOAuth2Request tenantAwareOAuth2Request = new TenantAwareOAuth2Request(authentication.getOAuth2Request());
		 tenantAwareOAuth2Request.setTenant((String) map.get("tenant_id"));
		 return new OAuth2Authentication(tenantAwareOAuth2Request, authentication.getUserAuthentication());
		    
	}


}
