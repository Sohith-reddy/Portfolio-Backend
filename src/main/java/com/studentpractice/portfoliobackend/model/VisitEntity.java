package com.studentpractice.portfoliobackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


import java.time.LocalTime;

@Entity
@Table(name = "visits")
@AllArgsConstructor
@NoArgsConstructor
public class VisitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ipAddress;
    private String city;
    private String ref;
    private LocalDateTime visitedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getVisitedTime() {
        return visitedTime;
    }

    public void setVisitedTime(LocalDateTime visitedTime) {
        this.visitedTime = visitedTime;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
