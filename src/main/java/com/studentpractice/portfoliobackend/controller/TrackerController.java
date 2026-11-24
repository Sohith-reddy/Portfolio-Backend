package com.studentpractice.portfoliobackend.controller;

import com.studentpractice.portfoliobackend.model.VisitEntity;
import com.studentpractice.portfoliobackend.repository.VisitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/track")
@CrossOrigin(origins = {
        "https://sohithreddy.dev",
        "https://www.sohithreddy.dev",
        "http://localhost:3000"
})
public class TrackerController {

    private static final Logger log = LoggerFactory.getLogger(TrackerController.class);

    private final VisitRepository visitRepository;

    public TrackerController(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> trackVisit(@RequestBody VisitEntity visit,
            HttpServletRequest request) {
        try {
            visit.setId(null);
            visit.setIpAddress(limit(request.getRemoteAddr(), 64));
            visit.setVisitedTime(LocalDateTime.now());
            visit.setCity(limit(visit.getCity(), 255));
            visit.setRegion(limit(visit.getRegion(), 255));
            visit.setCountry(limit(visit.getCountry(), 255));
            visit.setRef(limit(visit.getRef(), 255));
            visit.setPage(limit(visit.getPage(), 500));
            visit.setReferrer(limit(visit.getReferrer(), 1000));

            VisitEntity saved = visitRepository.save(visit);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("id", saved.getId(), "message", "Visit Tracked Successfully"));
        } catch (Exception e) {
            log.error("Could not save visit", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not track visit"));
        }
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        return Map.of(
                "total", visitRepository.count(),
                "last30Days", visitRepository.countByVisitedTimeAfter(LocalDateTime.now().minusDays(30)));
    }

    private static String limit(String value, int max) {
        if (value == null)
            return null;
        return value.length() > max ? value.substring(0, max) : value;
    }
}