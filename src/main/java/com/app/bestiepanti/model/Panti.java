package com.app.bestiepanti.model;

import java.math.BigInteger;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "pantis")
public class Panti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
 
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserApp user;
 
    private List<String> image;

    @Column(columnDefinition = "TEXT")
    private String description;
 
    private String phone;
 
    @Column(name = "donation_types")
    private List<String> donationTypes;
 
    @Column(name = "is_urgent")
    private Integer isUrgent;
   
    private String address;

    private String region;

    private String maps;
   
}
