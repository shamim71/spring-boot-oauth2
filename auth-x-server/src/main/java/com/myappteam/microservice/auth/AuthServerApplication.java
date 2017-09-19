package com.myappteam.microservice.auth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



/**
 * The Main Spring Boot Application class that starts the authorization
 * server.</br>
 * </br>
 * 
 * Note that the server is also a Eureka client so as to register with the
 * Eureka server and be auto-discovered by other Eureka clients.
 *
 * @author Shamim Ahmmed
 */


//@EnableEurekaClient
@SpringBootApplication
public class AuthServerApplication { 



    @Configuration
    @Order(-20)
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    	@Autowired
    	SimpleAuthenticationProvider authProvider;
    	

        @Override
        @Bean
		public
        AuthenticationManager authenticationManagerBean() throws Exception {
          return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

        	http//.csrf().disable()
			.logout()
			.logoutUrl("/logout")
			.deleteCookies("JSESSIONID").and()	
                    .formLogin().loginPage("/login").permitAll()
                    .and()
                    .requestMatchers()
                    //specify urls handled
                    .antMatchers("/login", "/logout","/tokens/**","/oauth/authorize", "/oauth/confirm_access")
                    .antMatchers("/fonts/**", "/js/**", "/css/**")
                    .and()
                    .authorizeRequests()
                    .antMatchers("/fonts/**", "/js/**", "/css/**").permitAll()
                    .antMatchers("/oauth/token/revokeById/**").permitAll()
                    .antMatchers("/tokens/**").permitAll()
                    .anyRequest().authenticated();


        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        	auth.authenticationProvider(authProvider);
        }
    }

    @Configuration
    @EnableAuthorizationServer
    static class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        @Qualifier("authenticationManagerBean")
        AuthenticationManager authenticationManager;

        @Autowired
        ClientDetailsService clientDetailsService;
        
    	@Autowired
    	private DataSource dataSource;

        @Override
       public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.jdbc(dataSource);
/*                    .withClient("web-app")
                    .scopes("read")
                    .autoApprove(true)
                    .accessTokenValiditySeconds(600)
                    .refreshTokenValiditySeconds(600)
                    .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code");*/
            
    		
        }

        @Override
       public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.
            tokenStore(tokenStore()).
           // userApprovalHandler(userApprovalHandler()).
            tokenEnhancer(accessTokenConverter()).
            approvalStore(jdbcApprovalStore()).
            tokenServices(tokenServices()).
            authenticationManager(authenticationManager);
        }
        
        @Bean
        JdbcApprovalStore jdbcApprovalStore() {
            return new JdbcApprovalStore(dataSource);
        }

        @Bean
        public TokenStore tokenStore() {
        	JwtTokenStore jwtTokenStore = new JwtTokenStore(accessTokenConverter());
        	 jwtTokenStore .setApprovalStore(jdbcApprovalStore());
           return jwtTokenStore;
        }
        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
           
            defaultTokenServices.setTokenStore(tokenStore());
            defaultTokenServices.setSupportRefreshToken(true);
            defaultTokenServices.setTokenEnhancer(accessTokenConverter());
            return defaultTokenServices;
        }
        @Bean
        protected JwtAccessTokenConverter accessTokenConverter() {
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                    new ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
           return converter;
        }
/*        @Bean
        @Autowired
        public TokenStoreUserApprovalHandler userApprovalHandler(){

            TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
            handler.setTokenStore(tokenStore());
            handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
            handler.setClientDetailsService(clientDetailsService);
            return handler;
        }*/
    }


    public static void main(String[] args) {
      
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
