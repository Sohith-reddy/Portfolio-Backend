package com.studentpractice.portfoliobackend.dto;

import jakarta.validation.constraints.Size;

public record TrackRequest(
        @Size(max = 100) String city,
        @Size(max = 100) String region,
        @Size(max = 100) String country,
        @Size(max = 100) String ref,
        @Size(max = 255) String page,
        @Size(max = 500) String referrer
) {
}
