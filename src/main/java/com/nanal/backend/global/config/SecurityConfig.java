package com.nanal.backend.global.config;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.global.security.CustomAccessDeniedHandler;
import com.nanal.backend.global.security.filter.ExceptionFilter;
import com.nanal.backend.global.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ExceptionFilter exceptionFilter;
    private final JwtAuthFilter jwtAuthFilter;
    private final AccessDeniedHandler accessDeniedHandler;

    // Filter 제외 요청들
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests().antMatchers("/main", "/auth/**", "/docs/**", "/favicon.ico", "/actuator/**", "/health", "/test/**").permitAll()
                .antMatchers("/onBoarding").hasRole("ONBOARDER")
                .anyRequest().hasRole("USER");

        http.exceptionHandling()
                        .accessDeniedHandler(accessDeniedHandler);

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // ControllerAdvice 로는 filter 에서 발생하는 예외를 다룰 수 없으므로 ExceptionFilter 추가.
                .addFilterBefore(exceptionFilter, JwtAuthFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
