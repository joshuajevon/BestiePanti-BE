package com.app.bestiepanti.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.bestiepanti.model.Role;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.RoleRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role role = roleRepository.findByName("ROLE_ADMIN");

        for (int i = 1; i <= 3; i++) {
            String email = "admin" + i + "@gmail.com";
            if (userRepository.findByEmail(email).isEmpty()) {
                UserApp user = new UserApp();
                user.setName("Admin" + i);
                user.setEmail(email); 
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRole(role);
                user.setIsGoogle(0);
                userRepository.save(user);
            }
        }
    }
    
}
