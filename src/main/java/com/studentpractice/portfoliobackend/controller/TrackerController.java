package com.studentpractice.portfoliobackend.controller;


import com.studentpractice.portfoliobackend.model.VisitEntity;
import com.studentpractice.portfoliobackend.repository.VisitRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/track")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@NoArgsConstructor
public class TrackerController {
    @Autowired
    private VisitRepository visitRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> trackVisit(@RequestBody VisitEntity visitEntity) {
        try {
            visitEntity.setVisitedTime(LocalDateTime.now());
            visitRepository.save(visitEntity);

            Map<String, Object> mp = new HashMap<>();
            mp.put("visit", visitEntity);
            mp.put("message", "Visit Tracked Successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(mp);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
