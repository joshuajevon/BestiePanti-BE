package com.app.bestiepanti.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.dto.request.UserRequest;
import com.app.bestiepanti.dto.response.UserResponse;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
    
    private final UserRepository userRepository;

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

    public UserResponse createUser(UserRequest userRequest) {

        UserApp user = new UserApp();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setRole(UserApp.ROLE_DONATUR);
        user.setPassword(userRequest.getPassword());
        userRepository.save(user);

        UserResponse userResponse = createUserResponse(user);
        return userResponse;
    }

    private UserResponse createUserResponse(UserApp userApp){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userApp.getId());
        userResponse.setName(userApp.getName());
        userResponse.setEmail(userApp.getEmail());
        userResponse.setRole(userApp.getRole());
        return userResponse;
    }

    
}
