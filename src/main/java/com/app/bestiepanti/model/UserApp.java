package com.app.bestiepanti.model;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "users")
public class UserApp implements UserDetails{

    public static final String ROLE_DONATUR = "DONATUR";
    public static final String ROLE_PANTI = "PANTI";
    public static final String ROLE_ADMIN = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    private String name;

    private String email;

    private String password;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "role_id", referencedColumnName = "id") 
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
