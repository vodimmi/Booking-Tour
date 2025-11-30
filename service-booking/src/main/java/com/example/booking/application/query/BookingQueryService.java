package com.example.booking.application.query;

import com.example.booking.application.dto.BookingResponse;
import com.example.booking.application.mapper.BookingResponseMapper;
import com.example.booking.domain.exception.BookingNotFoundException;
import com.example.booking.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingQueryService {
    private final BookingRepository bookingRepository;
    private final BookingResponseMapper mapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String authServiceBaseUrl = System.getenv("AUTH_API_BASE_URL") != null
            ? System.getenv("AUTH_API_BASE_URL")
            : "http://service-auth:8080";

    public BookingResponse getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    public List<BookingResponse> getAllBookings() {
        List<BookingResponse> results = bookingRepository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        enrichWithUser(results);
        return results;
    }

    public List<BookingResponse> getAllBookings(int page, int limit) {
        List<BookingResponse> results = bookingRepository.findAll(page, limit).stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        enrichWithUser(results);
        return results;
    }

    public List<BookingResponse> getAllBookings(int page, int limit, String search) {
        return getAllBookings(page, limit);
    }

    private void enrichWithUser(List<BookingResponse> results) {
        if (results == null || results.isEmpty()) return;
        for (BookingResponse r : results) {
            // Fetch user info
            if (r.getUserId() != null) {
                try {
                    String url = authServiceBaseUrl + "/api/users/" + r.getUserId();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> user = restTemplate.getForObject(url, Map.class);
                    if (user != null) {
                        Object fullName = user.get("fullName");
                        Object email = user.get("email");
                        r.setCustomerName(fullName != null ? fullName.toString() : (email != null ? email.toString() : null));
                        r.setCustomerEmail(email != null ? email.toString() : null);
                    }
                } catch (Exception ignored) {
                    // ignore failures to fetch user; FE will show N/A
                }
            }
            
            // Fetch tour start date
            if (r.getTourId() != null) {
                try {
                    String tourUrl = "http://service-tour:8081/api/tours/" + r.getTourId();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> tour = restTemplate.getForObject(tourUrl, Map.class);
                    if (tour != null) {
                        Object startDate = tour.get("startDate");
                        r.setTourStartDate(startDate != null ? startDate.toString() : null);
                    }
                } catch (Exception ignored) {
                    // ignore failures to fetch tour; FE will show N/A
                }
            }
        }
    }
}
