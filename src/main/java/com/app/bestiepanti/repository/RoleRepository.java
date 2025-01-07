package com.app.bestiepanti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.bestiepanti.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
