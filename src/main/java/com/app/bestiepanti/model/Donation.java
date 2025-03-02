package com.app.bestiepanti.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CANCELLED = "CANCELLED";

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

    @Column(name = "donation_types")
    private List<String> donationTypes;

    private List<String> image;
    
    private String notes;

    private String number;

    private String status;

    @Column(name = "inserted_timestamp")
    private LocalDateTime insertedTimestamp;

    @Column(name = "verified_timestamp")
    private LocalDateTime verifiedTimestamp;

}
