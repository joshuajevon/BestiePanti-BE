package com.app.bestiepanti.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "donatur_id", referencedColumnName = "id")
    private UserApp donaturId;

    @ManyToOne
    @JoinColumn(name = "panti_id", referencedColumnName = "id")
    private UserApp pantiId;

    @Column(name = "donation_date")
    private LocalDate donationDate;

    @Column(name = "is_onsite")
    private Integer isOnsite;

    @Column(name = "donation_type")
    private List<String> donationType;

    private List<String> image;

    private String status;
}
