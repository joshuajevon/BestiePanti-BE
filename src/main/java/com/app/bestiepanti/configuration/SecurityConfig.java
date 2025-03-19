package com.app.bestiepanti.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.bestiepanti.exception.CustomAccessDeniedHandler;
import com.app.bestiepanti.filter.JwtAuthencationFilter;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthencationFilter jwtAuthencationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CorsConfig corsConfig;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .disable())
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/",
                            "/api/v1/login",
                            "/api/v1/register",
                            "/api/v1/user",
                            "/api/v1/panti/view",
                            "/api/v1/panti/view/**",
                            "/api/v1/panti/urgent",
                            "/api/v1/message/view",
                            "/api/v1/message/view/**",
                            "/api/v1/donation/fund/view",
                            "/api/v1/donation/fund/view/**",
                            "/api/v1/donation/nonfund/view",
                            "/api/v1/donation/nonfund/view/**",
                            "/storage/image/**",
                            "/storage/qris/**",
                            "/storage/profile/**",
                            "/storage/donation/**"
                            ).permitAll(); // no auth

                    registry.requestMatchers("/api/v1/admin/**",
                            "/api/v1/panti/create",
                            "/api/v1/panti/update/**",
                            "/api/v1/panti/delete/**",
                            "/api/v1/donatur/update/**",
                            "/api/v1/donatur/view",
                            "/api/v1/donatur/delete/**",
                            "/api/v1/donation/fund/delete/**",
                            "/api/v1/donation/nonfund/delete/**").hasRole("ADMIN");

                    registry.requestMatchers("/api/v1/donatur/",
                            "/api/v1/donatur/update/**",
                            "/api/v1/donatur/view/**",
                            "/api/v1/message/create/**",
                            "/api/v1/donation/fund/create/**",
                            "/api/v1/donation/nonfund/create/**",
                            "/storage/donation/**",
                            "/donatur/profile/update").hasRole("DONATUR");

                    registry.requestMatchers("/api/v1/panti/",
                            "/api/v1/panti/update/**",
                            "/api/v1/donation/fund/verify/**",
                            "/api/v1/donation/nonfund/verify/**",
                            "/api/v1/donation/fund/get/**",
                            "/api/v1/donation/nonfund/get/**",
                            "/api/v1/message/accept/**",
                            "/api/v1/message/delete/**",
                            "/storage/donation/**",
                            "/panti/profile/update").hasRole("PANTI");

                    registry.anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthencationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Logout successful");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        ;
        return httpSecurity.build();
    }
}
