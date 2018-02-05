package com.bitplexer.microservices.resource;

import java.security.Principal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleResourceController {

	private static final Logger logger = LoggerFactory.getLogger(SampleResourceController.class);
	
	public static TenantAwareOAuth2Request getOAuth2RequestFromAuthentication() {
	    Authentication authentication = getAuthentication();
	    return getTenantAwareOAuth2Request(authentication);
	}
	 
	private static TenantAwareOAuth2Request getTenantAwareOAuth2Request(Authentication authentication) {
	    if (!authentication.getClass().isAssignableFrom(OAuth2Authentication.class)) {
	        throw new RuntimeException("unexpected authentication object, expected OAuth2 authentication object");
	    }
	    return (TenantAwareOAuth2Request) ((OAuth2Authentication) authentication).getOAuth2Request();
	}
	 
	private static Authentication getAuthentication() {
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    return securityContext.getAuthentication();
	}
	@RequestMapping("/")
	public Message home() {
	     
	    	Message msg = new Message();
	    	msg.setId(UUID.randomUUID().toString());
	    	msg.setResource(UUID.randomUUID().toString());
	    	return msg;

	}
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Message readFoo(@PathVariable String id, Principal principal) {
     
    	TenantAwareOAuth2Request auth = getOAuth2RequestFromAuthentication();
    	logger.error("User Tenant Id: "+auth.getTenant());
    	
    	Message msg = new Message();
    	msg.setId(id);
    	msg.setUser(principal.getName());
    	msg.setResource(UUID.randomUUID().toString());
    	return msg;
    }
	/**
	 * Return the principal identifying the logged in user
	 * @param user
	 * @return
	 */
	@RequestMapping("/user")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}
}
