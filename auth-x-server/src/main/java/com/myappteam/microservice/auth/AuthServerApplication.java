package com.myappteam.microservice.auth;


import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.myappteam.microservice.auth.services.CustomTokenService;
import com.myappteam.microservice.auth.services.TokenBlackListService;



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
@EnableAsync
public class AuthServerApplication { 

	private static final Logger logger = LoggerFactory.getLogger(AuthServerApplication.class);

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


            http
            .logout()
            //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll().and()
            
                    .formLogin().loginPage("/login").permitAll()
                    .and().httpBasic().and()
                    .requestMatchers()
                    //specify urls handled
                    .antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access") //why need to permit /oauth/authorize 
                    .antMatchers("/fonts/**", "/js/**", "/css/**","/webjars/**")
                    .and()
                    .authorizeRequests()
                    .antMatchers("/fonts/**", "/js/**", "/css/**","/webjars/**").permitAll()
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
    	CustomUserDetailsService customUserDetailsService;
    	
        @Value("classpath:schema.sql")
	    private Resource schemaScript;

	    @Value("classpath:data.sql")
	    private Resource dataScript;
	    
    	@Autowired
    	DataSource dataSource;

        @Override
       public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        	clients.withClientDetails(clientDetailsService());
        }

        @Override
       public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.
            tokenStore(tokenStore()).
            tokenServices(tokenServices()).
           // userApprovalHandler(userApprovalHandler()).
            tokenEnhancer(jwtTokenEnhancer()).

            authenticationManager(authenticationManager).userDetailsService(customUserDetailsService);
        }
/*        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {

            oauthServer
                    // we're allowing access to the token only for clients with 'ROLE_TRUSTED_CLIENT' authority
                    .tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                    .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
        }*/
        
        @Bean
        public DataSourceInitializer dataSourceInitializer() {
            final DataSourceInitializer initializer = new DataSourceInitializer();
            initializer.setDataSource(dataSource);
            initializer.setDatabasePopulator(databasePopulator());
            return initializer;
        }

        private DatabasePopulator databasePopulator() {
            final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
           // populator.addScript(schemaScript);
            //populator.addScript(dataScript);
            return populator;
   }
        @Bean
        public TokenStore tokenStore() {
        	JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtTokenEnhancer());
        	
        	//jwtTokenStore .setApprovalStore(jdbcApprovalStore());
           return jwtTokenStore;
        }
        @Bean
        JdbcApprovalStore jdbcApprovalStore() {
            return new JdbcApprovalStore(dataSource);
        }
        
        @Autowired
        private TokenBlackListService blackListService;

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
        	CustomTokenService tokenService = new CustomTokenService(blackListService);
            tokenService.setTokenStore(tokenStore());
            tokenService.setSupportRefreshToken(true);
            tokenService.setTokenEnhancer(jwtTokenEnhancer());
          
            return tokenService;
        }

        
//        @Bean
//        @Primary
//        public DefaultTokenServices tokenServices() {
//            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//           
//            defaultTokenServices.setTokenStore(tokenStore());
//            defaultTokenServices.setSupportRefreshToken(true);
//            defaultTokenServices.setTokenEnhancer(jwtTokenEnhancer());
//            return defaultTokenServices;
//        }
        @Bean
        protected JwtAccessTokenConverter jwtTokenEnhancer() {
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                    new ClassPathResource("jwt_key_store.jks"), "MyAppPass123#".toCharArray());
            
            JwtAccessTokenConverter converter = new MultiTenantAccessTokenConverter();
            
            DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
            DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
            userAuthenticationConverter.setUserDetailsService(customUserDetailsService);
            tokenConverter.setUserTokenConverter(userAuthenticationConverter);
            
            converter.setAccessTokenConverter(tokenConverter);
            
            converter.setKeyPair(keyStoreKeyFactory.getKeyPair("myappteam.com"));
            logger.debug("---------------------------------------------------------------------");
            logger.debug("");
           return converter;
        }

        @Bean
        public JdbcClientDetailsService clientDetailsService() {
            return new JdbcClientDetailsService(dataSource);
        }

    }


    public static void main(String[] args) {
      
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
