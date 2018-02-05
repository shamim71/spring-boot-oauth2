package com.myappteam.microservice.auth;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myappteam.microservice.auth.dao.UserInfo;
import com.myappteam.microservice.auth.dto.CustomUserPrincipal;
import com.myappteam.microservice.auth.repositories.UserInfoRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	UserInfoRepository userRepository;

	
	//@Value("#{'${client.names}'.split(',')}")
	@Value("#{'${client.names}'.split(',')}")
	private List<String> clientNames;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (username == null || username.isEmpty()) {
			throw new UsernameNotFoundException("Username is empty");
		}

		Authentication authentication = SecurityContextHolder.getContext()
		        .getAuthentication();

		Object clientPrincipal = authentication.getPrincipal();
		 if (clientPrincipal instanceof User == false) {
			 throw new UsernameNotFoundException("Unauthorized client_id or username not found: " + username);
		 }
		  
		  String clientId  = ((User) clientPrincipal).getUsername();

		  if(clientNames.contains(clientId)){

			    Optional<UserInfo>   userInfo = userRepository.findByEmail(username);
			        if ( userInfo.isPresent() == false) {
			        	logger.error("Unauthorized client_id or username not found: " + username);
			        	 throw new UsernameNotFoundException("Unauthorized client_id or username not found: " + username);
			        }
			        
			       CustomUserPrincipal userPrincipal = new CustomUserPrincipal(userInfo.get());
			        
		    	return userPrincipal;
		  }
		   throw new UsernameNotFoundException("Unauthorized client_id or username not found: " + username);
	}

}
