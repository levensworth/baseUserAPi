package com.yaatc.Controller.Config;


import com.yaatc.Controller.Config.JSONWebToken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@ComponentScan("com.yaatc.Controller.Config")
@Configuration
public class WebAuth extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JSONWebTokenAuthenticationProvider jwtAuthProvider;

    @Autowired
    private JSONAuthenticationSuccessHandler JSONAuthenticationSuccessHandler;

    @Autowired
    private com.yaatc.Controller.Config.JSONWebToken.JSONAuthenticationFailureHandler JSONAuthenticationFailureHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public JSONWebTokenAuthenticationFilter jwtAuthFilter() throws Exception {
        JSONWebTokenAuthenticationFilter filter = new JSONWebTokenAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setRequiresAuthenticationRequestMatcher(protectedEndpointsMatcher());
        filter.setAuthenticationSuccessHandler(new JSONWebTokenAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new JSONWebTokenAuthenticationFailureHandler());
        return filter;
    }

    @Bean
    public JSONAuthenticationFilter jsonAuthFilter() throws Exception {
        JSONAuthenticationFilter filter = new JSONAuthenticationFilter();
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login",
                "POST"));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(JSONAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(JSONAuthenticationFailureHandler);
        return filter;
    }

    @Bean
    public RequestMatcher protectedEndpointsMatcher() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/**", "POST"),
                new AntPathRequestMatcher("/**", "PUT"),
                new AntPathRequestMatcher("/**", "DELETE"),
                new AntPathRequestMatcher("/**", "PATCH"),
                optionallyAuthenticatedEndpointsMatcher()
        );
    }

    /**
     * Some endpoints may optionally accept authentication, and possibly return different responses in that case. The
     * endpoints matched by this matcher MUST also work without authentication, although the response may be different
     * than if authenticated.
     *
     * @return A matcher for optionally authenticated endpoints.
     */
    @Bean
    public RequestMatcher optionallyAuthenticatedEndpointsMatcher() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/about-us", "GET"),
                new AntPathRequestMatcher("/users", "POST"),
                new AntPathRequestMatcher("/users/reset-password", "POST"),
                new AntPathRequestMatcher("/users/reset-password", "GET"),
                new AntPathRequestMatcher("/users/change-password", "POST")
        );
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService)
                .addFilterBefore(jsonAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                // Use json login for initial authentication.
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                // Protect all other necessary endpoints with JWT.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and().authorizeRequests()
                //.antMatchers("/about-us", "/**/permit-all/**").permitAll()
                //.antMatchers("/login", "/", "/forgot-password", "/public-login", "/reset-password", "/regitration-confirm").anonymous()
                //.antMatchers("/change-user/**").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
                //.antMatchers("/user/menu/**", "/user/order/order-placed").access("hasAuthority('CUSTOMER') and !hasAuthority('READY_TO_PAY')")
                //.antMatchers("/user/order/**").hasAuthority("CUSTOMER")
                //.antMatchers("/user/menu-choose-option").access("!hasAuthority('CUSTOMER') and hasAnyRole('USER', 'PUBLIC_USER')")
                //.antMatchers("/public-user/login").access("hasAuthority('CUSTOMER') and hasRole('PUBLIC_USER')")
                //.antMatchers("/public-user/**").hasRole("PUBLIC_USER")
                //.antMatchers("/user/**").hasAnyRole("USER", "PUBLIC_USER")
                //.antMatchers("/chef/**").hasAnyRole("ADMIN", "CHEF")
                //.antMatchers("/admin/**").hasRole("ADMIN")
                //.antMatchers("/**/private/**").authenticated()
                //.anyRequest().denyAll()
                .and().logout().disable()
                .rememberMe().disable()
                .csrf().disable();
    }

    @Autowired
    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthProvider)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                //.antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers(HttpMethod.POST, "/about-us");
    }
}