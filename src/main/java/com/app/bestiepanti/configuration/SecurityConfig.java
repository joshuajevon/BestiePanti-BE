package com.app.bestiepanti.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    // private final AuthenticatedUserFilter authenticatedUserFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf
                        .disable())
                // .formLogin(httpForm -> {
                // httpForm.loginPage("/login").permitAll();
                // httpForm.defaultSuccessUrl("/", true);
                // httpForm.failureUrl("/login?error=true");
                // })
                // .logout(logout -> logout
                // .logoutUrl("/logout")
                // .logoutSuccessUrl("/login?logout")
                // .invalidateHttpSession(true)
                // .deleteCookies("JSESSIONID")
                // )
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/", "/login", "/register", "/tentang-kami", "/cara-donasi", "/css/**",
                            "/js/**",
                            "/assets/**").permitAll(); // no auth
                    registry.requestMatchers("/admin/**").hasRole("ADMIN");
                    registry.requestMatchers("/donatur/**").hasRole("DONATUR");
                    registry.requestMatchers("/panti/**").hasRole("PANTI");
                    registry.anyRequest().authenticated();
                })
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthencationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Logout successful");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        ;
        return httpSecurity.build();

        // return httpSecurity
        // .csrf(AbstractHttpConfigurer::disable)
        // // .formLogin(httpForm -> {
        // // httpForm.loginPage("/login").permitAll();
        // // httpForm.defaultSuccessUrl("/", true);
        // // httpForm.failureUrl("/login?error=true");
        // // })
        // // .logout(logout -> logout
        // // .logoutUrl("/logout")
        // // .logoutSuccessUrl("/login?logout")
        // // .invalidateHttpSession(true)
        // // .deleteCookies("JSESSIONID")
        // // )
        // .authorizeHttpRequests(registry -> {
        // registry.requestMatchers("/","/login","/register", "/css/**",
        // "/js/**").permitAll(); // no auth
        // registry.requestMatchers("/admin/**").hasRole("ADMIN");
        // registry.requestMatchers("/donatur/**").hasRole("DONATUR");
        // registry.requestMatchers("/panti/**").hasRole("PANTI");
        // registry.anyRequest().authenticated();
        // })
        // // .addFilterBefore(authenticatedUserFilter,
        // UsernamePasswordAuthenticationFilter.class)
        // .build();
    }
}
