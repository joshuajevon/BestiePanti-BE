package com.app.bestiepanti.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserApp> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            var userObj = user.get();
            Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_" + userObj.getRole().getName()));

            return User.builder()
                    .username(userObj.getEmail())
                    .password(userObj.getPassword())
                    .authorities(authorities)
                    .build();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}

