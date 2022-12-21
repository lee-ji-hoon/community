package com.community.infra.config;

import com.community.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                )
                .authorizeRequests(
                        request -> request
                                .mvcMatchers("/login", "/sign-up", "/check-email", "/check-email-token",
                                        "/email-login", "/check-email-login", "/login-link", "/email-login-view").permitAll()
                                .antMatchers("/manager/*").hasAnyRole("ADMIN")
                );
        return http.build();
    }


    /*@Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .mvcMatchers("/login", "/sign-up", "/check-email", "/check-email-token",
                        "/email-login", "/check-email-login", "/login-link", "/email-login-view").permitAll()
                .antMatchers("/study/*", "/board/*", "/council/*", "/", "/logout").authenticated()
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
                .antMatchers("/manager/*").hasAnyRole("ADMIN")
                .anyRequest().authenticated();
        *//*
        * 기존 formLogin 로직
        http.formLogin()
                .loginPage("/login")
                .permitAll();
        *//*
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");

        // 인증 필요시 로그인 페이지와 로그인 성공시 리다이랙팅 경로 지정
        http.formLogin().loginPage("/login").defaultSuccessUrl("/", true);
        // 로그인이 수행될 uri 매핑 (post 요청이 기본)
        http.formLogin().loginProcessingUrl("/login").defaultSuccessUrl("/", true);
        http.exceptionHandling().accessDeniedPage("/");

        http.userDetailsService(accountService);

        http.rememberMe()
                .userDetailsService(accountService)
                .tokenRepository(tokenRepository());
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/node_modules/**", "/assets/**", "/error", "/images/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }*/
}
