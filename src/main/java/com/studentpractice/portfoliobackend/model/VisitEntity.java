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

    @Column(length = 64)
    private String ipAddress;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String region;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String ref;

    @Column(length = 255)
    private String page;

    @Column(length = 500)
    private String referrer;

    @Column(length = 500)
    private String userAgent;

    // Mapped to the column name used by earlier versions so existing rows keep working.
    @Column(name = "visited_time")
    private LocalDateTime visitedAt;
}
