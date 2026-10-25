package com.studentpractice.portfoliobackend.controller;

import com.studentpractice.portfoliobackend.model.VisitEntity;
import com.studentpractice.portfoliobackend.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TrackerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitRepository visitRepository;

    @BeforeEach
    void clean() {
        visitRepository.deleteAll();
    }

    @Test
    void recordsVisitWithForwardedIp() throws Exception {
        mockMvc.perform(post("/api/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Forwarded-For", "203.0.113.7, 10.0.0.1")
                        .header("User-Agent", "JUnit")
                        .content("""
                                {"city":"Hyderabad","region":"Telangana","country":"India","ref":"acme","page":"/"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber());

        List<VisitEntity> visits = visitRepository.findAll();
        assertThat(visits).hasSize(1);
        VisitEntity visit = visits.get(0);
        assertThat(visit.getIpAddress()).isEqualTo("203.0.113.7");
        assertThat(visit.getCity()).isEqualTo("Hyderabad");
        assertThat(visit.getRef()).isEqualTo("acme");
        assertThat(visit.getUserAgent()).isEqualTo("JUnit");
        assertThat(visit.getVisitedAt()).isNotNull();
    }

    @Test
    void defaultsMissingRefToUnknown() throws Exception {
        mockMvc.perform(post("/api/track").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated());

        assertThat(visitRepository.findAll()).singleElement()
                .extracting(VisitEntity::getRef).isEqualTo("unknown");
    }

    @Test
    void rejectsOversizedFields() throws Exception {
        String longRef = "x".repeat(101);
        mockMvc.perform(post("/api/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ref\":\"" + longRef + "\"}"))
                .andExpect(status().isBadRequest());

        assertThat(visitRepository.count()).isZero();
    }

    @Test
    void allowsConfiguredOriginOnly() throws Exception {
        mockMvc.perform(options("/api/track")
                        .header("Origin", "https://portfolio.example.com")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://portfolio.example.com"));

        mockMvc.perform(options("/api/track")
                        .header("Origin", "https://evil.example.com")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
