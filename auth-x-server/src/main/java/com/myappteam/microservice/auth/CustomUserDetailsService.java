package com.myappteam.microservice.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myappteam.microservice.auth.dao.UserInfo;
import com.myappteam.microservice.auth.dao.UserInfoRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	UserInfoRepository userService;

	
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
		    	UserInfo  user = userService.findByEmail(username);
		    	Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		      	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getCode());
		    	authorities.add(authority);
		    	
		    	UserDetails x = new User(username, "", authorities);
			    if (user != null){
			        return x;
			    }
		  }
		   throw new UsernameNotFoundException("Unauthorized client_id or username not found: " + username);
	}

}
