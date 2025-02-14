package com.app.bestiepanti.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.bestiepanti.filter.JwtAuthencationFilter;

import jakarta.servlet.http.HttpServletResponse;

// import com.app.bestiepanti.middleware.AuthenticatedUserFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthencationFilter jwtAuthencationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CorsConfig corsConfig;

    // private final AuthenticatedUserFilter authenticatedUserFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())) 
                .csrf(csrf -> csrf
                        .disable())
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/", 
                                            "/api/v1/login", 
                                            "/api/v1/register"
                            ).permitAll(); // no auth
                    registry.requestMatchers("/api/v1/admin/**",
                                             "/api/v1/panti/create",
                                             "/api/v1/panti/update/**",
                                             "/api/v1/panti/delete/**").hasRole("ADMIN");
                    registry.requestMatchers("/api/v1/donatur/**").hasRole("DONATUR");
                    registry.requestMatchers("/api/v1/panti/").hasRole("PANTI");
                    registry.anyRequest().authenticated();
                })
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
