package com.example.booking.application.service;

import com.example.booking.application.dto.TourInfoDto;
import com.example.booking.application.dto.TourTimelineDto;
import com.example.booking.application.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class ExternalServiceClient {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;
    private final String tourServiceUrl;

    public ExternalServiceClient(
            RestTemplate restTemplate,
            @Value("${services.auth.url}") String authServiceUrl,
            @Value("${services.tour.url}") String tourServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
        this.tourServiceUrl = tourServiceUrl;
    }

    /**
     * Fetch user information from auth service
     * 
     * @param userId User ID
     * @return User information
     */
    public UserInfoDto getUserInfo(Long userId) {
        try {
            String url = authServiceUrl + "/api/users/" + userId;
            log.info("Fetching user info from: {}", url);

            ResponseEntity<UserInfoDto> response = restTemplate.getForEntity(url, UserInfoDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch user info for userId: {}", userId, e);
            throw new RuntimeException("Failed to fetch user information", e);
        }
    }

    /**
     * Fetch tour information from tour service
     * 
     * @param tourId Tour ID
     * @return Tour information
     */
    public TourInfoDto getTourInfo(Long tourId) {
        try {
            String url = tourServiceUrl + "/api/tours/" + tourId;
            log.info("Fetching tour info from: {}", url);

            ResponseEntity<TourInfoDto> response = restTemplate.getForEntity(url, TourInfoDto.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch tour info for tourId: {}", tourId, e);
            throw new RuntimeException("Failed to fetch tour information", e);
        }
    }

    /**
     * Fetch tour timeline from tour service
     * 
     * @param tourId Tour ID
     * @return List of tour timeline activities
     */
    public List<TourTimelineDto> getTourTimeline(Long tourId) {
        try {
            String url = tourServiceUrl + "/api/tours/" + tourId + "/timeline";
            log.info("Fetching tour timeline from: {}", url);

            ResponseEntity<List<TourTimelineDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TourTimelineDto>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch tour timeline for tourId: {}", tourId, e);
            throw new RuntimeException("Failed to fetch tour timeline", e);
        }
    }
}
