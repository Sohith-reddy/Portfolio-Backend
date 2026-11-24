package com.studentpractice.portfoliobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
public class VisitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;

    private String city;
    private String region;
    private String country;

    private String ref;

    @Column(length = 500)
    private String page;

    @Column(length = 1000)
    private String referrer;

    private LocalDateTime visitedTime;
}