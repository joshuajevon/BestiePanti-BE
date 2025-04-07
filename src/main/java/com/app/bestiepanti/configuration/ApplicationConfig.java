package com.app.bestiepanti.configuration;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.app.bestiepanti.repository.UserRepository;

import jakarta.annotation.PostConstruct;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Configuration
@Data
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Value("${panti.image.upload-dir}")
    private String imageUploadDir;

    @Value("${panti.qris.upload-dir}")
    private String qrisUploadDir;

    @Value("${donation.image.upload-dir}")
    private String imageDonationUploadDir;

    @Value("${profile.image.upload-dir}")
    private String profileImageUploadDir;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${url.front-end.bestiepanti}")
    private String urlFrontEnd;

    @Value("${url.back-end.bestiepanti}")
    private String urlBackEnd;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @PostConstruct
    public void init() {
        createDirectory(imageUploadDir);
        createDirectory(qrisUploadDir);
        createDirectory(imageDonationUploadDir);
        createDirectory(profileImageUploadDir);
    }

    private void createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}

