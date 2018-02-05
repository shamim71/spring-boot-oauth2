package com.bitplexer.microservices.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;





@SpringBootApplication

public class ResourceApplication {

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
        @Override
        public void configure(ResourceServerSecurityConfigurer config) {
            config.tokenServices(defaultTokenServices());
            config.tokenStore(tokenStore());
        }
        @Bean
        public ResourceServerTokenServices defaultTokenServices() {
            final TenantAwareResourceServerTokenServices defaultTokenServices = new TenantAwareResourceServerTokenServices();
            defaultTokenServices.setTokenStore(tokenStore());

            return defaultTokenServices;
        }
     

        @Bean
        public TokenStore tokenStore() {
       	
        	JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtTokenEnhancer());

           return jwtTokenStore;
        }
        @Bean
        public JwtAccessTokenConverter jwtTokenEnhancer() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
            converter.setAccessTokenConverter(tokenConverter);
            return converter;
        }

    }
    
}

