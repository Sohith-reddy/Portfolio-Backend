package com.studentpractice.portfoliobackend.controller;

import com.studentpractice.portfoliobackend.dto.TrackRequest;
import com.studentpractice.portfoliobackend.dto.TrackResponse;
import com.studentpractice.portfoliobackend.model.VisitEntity;
import com.studentpractice.portfoliobackend.service.VisitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/track")
public class TrackerController {
    private final VisitService visitService;

    public TrackerController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrackResponse trackVisit(@Valid @RequestBody TrackRequest request, HttpServletRequest httpRequest) {
        VisitEntity visit = visitService.recordVisit(
                request,
                clientIp(httpRequest),
                httpRequest.getHeader(HttpHeaders.USER_AGENT));
        return new TrackResponse(visit.getId(), "Visit tracked successfully");
    }

    // Hosting platforms terminate TLS at a proxy, so the real client is the first X-Forwarded-For entry.
    private static String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
