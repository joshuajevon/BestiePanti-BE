package com.app.bestiepanti.model;

import java.math.BigInteger;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
}
