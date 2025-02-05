package com.app.bestiepanti.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.app.bestiepanti.model.Role;
import com.app.bestiepanti.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        String[] roleNames = {"ROLE_DONATUR", "ROLE_PANTI", "ROLE_ADMIN"};
        
        Arrays.stream(roleNames).forEach(roleName -> {
            if (roleRepository.findByName(roleName) == null) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        });
    }
}

