package com.myappteam.microservice.auth;

import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.myappteam.microservice.auth.dao.UserInfo;
import com.myappteam.microservice.auth.dto.CustomUserPrincipal;
import com.myappteam.microservice.auth.repositories.UserInfoRepository;
import com.myappteam.microservice.auth.utility.CryptographicUtility;



@Component
public class SimpleAuthenticationProvider implements AuthenticationProvider{

	private static final Logger logger = LoggerFactory.getLogger(SimpleAuthenticationProvider.class);
	
	@Autowired
	UserInfoRepository userRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
	    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
	    String username = String.valueOf(auth.getPrincipal());
	    String password = String.valueOf(auth.getCredentials());

	    logger.info("username:" + username);


	 // 1. Use the username to load the data for the user, including authorities and password.
	    Optional<UserInfo>   userInfo = userRepository.findByEmail(username);
       // Optional<Account> account = userRepository.findByUsername(username);
        if ( userInfo.isPresent() == false) {

           // throw new UsernameNotFoundException(String.format("Username[%s] not found", s));
            throw new BadCredentialsException("Invalid user password");
        }
        UserInfo user = userInfo.get();
        
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
		CustomUserPrincipal userPrincipal = new CustomUserPrincipal(user);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities()) ;
		
	    return token;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(
		          UsernamePasswordAuthenticationToken.class);
	}

}
