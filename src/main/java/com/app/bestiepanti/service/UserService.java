package com.app.bestiepanti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserApp> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getEmail())
                    .password(userObj.getPassword())
                    .build();    
        }else{
            throw new UsernameNotFoundException(email);
        }
    }

    public UserApp createUser(UserApp user) {
        user.setRole(UserApp.ROLE_DONATUR);
        return userRepository.save(user);
    }

    
}
