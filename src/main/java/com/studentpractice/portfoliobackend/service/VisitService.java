package com.studentpractice.portfoliobackend.service;

import com.studentpractice.portfoliobackend.dto.TrackRequest;
import com.studentpractice.portfoliobackend.model.VisitEntity;
import com.studentpractice.portfoliobackend.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class VisitService {
    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public VisitEntity recordVisit(TrackRequest request, String ipAddress, String userAgent) {
        VisitEntity visit = new VisitEntity();
        visit.setIpAddress(truncate(ipAddress, 64));
        visit.setCity(request.city());
        visit.setRegion(request.region());
        visit.setCountry(request.country());
        visit.setRef(request.ref() == null || request.ref().isBlank() ? "unknown" : request.ref());
        visit.setPage(request.page());
        visit.setReferrer(request.referrer());
        visit.setUserAgent(truncate(userAgent, 500));
        visit.setVisitedAt(LocalDateTime.now(ZoneOffset.UTC));
        return visitRepository.save(visit);
    }

    private static String truncate(String value, int max) {
        return value == null || value.length() <= max ? value : value.substring(0, max);
    }
}
