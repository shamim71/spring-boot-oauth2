package com.bitplexer.microservices.resource;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class TenantAwareResourceServerTokenServices implements ResourceServerTokenServices,InitializingBean{

	private static final Logger logger = LoggerFactory.getLogger(TenantAwareResourceServerTokenServices.class);
	
	private TokenStore tokenStore;
	
	public void setTokenStore(TokenStore tokenStore){
		this.tokenStore = tokenStore;
	}
	
	@Override
	public OAuth2Authentication loadAuthentication(String accessTokenValue)
			throws AuthenticationException, InvalidTokenException {

		OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
		
		
		if (accessToken == null) {
			throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
		}
		else if (accessToken.isExpired()) {
			tokenStore.removeAccessToken(accessToken);
			throw new InvalidTokenException("Access token expired: " + accessTokenValue);
		}

		OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
		if (result == null) {
			// in case of race condition
			throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
		}
		logger.info(result.getName());
		
		Map<String, Object> map =  accessToken.getAdditionalInformation();
		logger.info("Tenant Id: "+ map.get("tenant_id"));
		TenantAwareOAuth2Request tenantAwareOAuth2Request = new TenantAwareOAuth2Request(result.getOAuth2Request());
		tenantAwareOAuth2Request.setTenant((String) map.get("tenant_id"));
		 
		return new OAuth2Authentication(tenantAwareOAuth2Request, result.getUserAuthentication());

	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		logger.debug("Reading access token value ");
		return tokenStore.readAccessToken(accessToken);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		
	}

}
