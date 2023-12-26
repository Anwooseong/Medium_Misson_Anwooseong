package com.ll.medium.global.security;

import com.ll.medium.domain.member.service.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        form ->
                                form
                                        .loginPage("/members/login")
                                        .successHandler((request, response, authentication) -> response.sendRedirect("/"))
                                        .usernameParameter("loginId")
                                        .failureUrl("/members/login/error")
                )
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers("/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()
                                        .requestMatchers("/", "/members/**", "/b/**").permitAll()
                                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/post/list")).permitAll()
                                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/post/**")).permitAll()
                                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/post/{postId}/increaseHit")).permitAll()
                                        .anyRequest().authenticated()
                )
                .csrf(
                        csrf ->
                                csrf.ignoringRequestMatchers(
                                        "/h2-console/**"
                                )
                )
                .exceptionHandling(
                        except ->
                                except.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        ;

        return http.build();
    }
}
