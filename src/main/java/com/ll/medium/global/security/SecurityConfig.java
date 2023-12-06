package com.ll.medium.global.security;

import com.ll.medium.domain.member.service.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                                        .defaultSuccessUrl("/")
                                        .usernameParameter("loginId")
                                        .failureUrl("/members/login/error")
                )
                .logout(
                        logout ->
                                logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                                        .logoutSuccessUrl("/")
                )
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf(
                        csrf ->
                                csrf.ignoringRequestMatchers(
                                        "/h2-console/**"
                                )
                )
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                        .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        except ->
                                except.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
        ;

        return http.build();
    }
}
