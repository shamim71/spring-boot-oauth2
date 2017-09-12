package com.bitplexer.microservices.resource;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class ResourceApplication {

	@RequestMapping("/")
	public Message home() {
	     
	    	Message msg = new Message();
	    	msg.setId(UUID.randomUUID().toString());
	    	msg.setResource(UUID.randomUUID().toString());
	    	return msg;

	}
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Message readFoo(@PathVariable String id, Principal principal) {
     
    	Message msg = new Message();
    	msg.setId(id);
    	msg.setUser(principal.getName());
    	msg.setResource(UUID.randomUUID().toString());
    	return msg;
    }

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

    @Configuration
    @EnableResourceServer
    public static class ResourceServiceConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/**").hasAuthority("ROLE_READER");
        }

    }
    
}

class Message {
	private String id;
	private String resource;
	private String user;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

}
