package com.myappteam.microservice.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.myappteam.microservice.auth.dao.UserInfo;
import com.myappteam.microservice.auth.dao.UserInfoRepository;
import com.myappteam.microservice.auth.dto.UserInfoDto;
import com.myappteam.microservice.auth.service.impl.UserInfoServiceImpl;
import com.myappteam.microservice.auth.utility.CryptographicUtility;



@Component
public class SimpleAuthenticationProvider implements AuthenticationProvider{

	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	@Autowired
	UserInfoRepository userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
	    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
	    String username = String.valueOf(auth.getPrincipal());
	    String password = String.valueOf(auth.getCredentials());

	    logger.info("username:" + username);


	 // 1. Use the username to load the data for the user, including authorities and password.
	    UserInfo user = userService.findByEmail(username);
	    
	    if(user == null){
	    	throw new BadCredentialsException("Invalid user password");
	    }
	    
		byte[] sharedSecret = Base64.decodeBase64(user.getSalt());

		byte[] hashedSharedSecret = CryptographicUtility.encode(password.toCharArray(), sharedSecret);

		if (hashedSharedSecret == null) {
			throw new BadCredentialsException("Bad Credentials");
		}
		String encodedSharedSecreet = Base64
				.encodeBase64String(hashedSharedSecret);

		if (!user.getHashedPassword().equals(encodedSharedSecreet)) {
			throw new BadCredentialsException("Invalid user password");
		}		
		UserInfoDto userPrincipal = new UserInfoDto(user);
        
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
       
/*        for(String str: userPrincipal.getPermissions()){
        	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(str);
        	authorities.add(authority);
        }*/
    	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getCode());
    	authorities.add(authority);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal.getEmail(), null, authorities) ;
		
	    return token;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(
		          UsernamePasswordAuthenticationToken.class);
	}

}
