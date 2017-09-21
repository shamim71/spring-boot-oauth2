package com.myappteam.microservice.auth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.sql.DataSource;

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
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
        	
           return jwtTokenStore;
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
                    new ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
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
