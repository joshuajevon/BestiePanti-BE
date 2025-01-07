package com.app.bestiepanti.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "roles")
public class Role {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private Long id; 
    
    @Column(unique = true) 
    private String name;
}
