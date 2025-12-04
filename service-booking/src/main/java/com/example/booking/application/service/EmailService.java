package com.example.booking.application.service;

import com.example.booking.application.dto.TourInfoDto;
import com.example.booking.application.dto.TourTimelineDto;
import com.example.booking.application.dto.UserInfoDto;
import com.example.booking.application.util.GoogleCalendarUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    /**
     * Send booking confirmation email with tour timeline and Google Calendar link
     * 
     * @param userInfo       User information
     * @param tourInfo       Tour information
     * @param timelines      Tour timeline activities
     * @param numberOfPeople Number of participants
     * @param totalPrice     Total booking price
     * @param startDate      Tour start date
     * @param endDate        Tour end date
     */
    public void sendBookingConfirmationEmail(
            UserInfoDto userInfo,
            TourInfoDto tourInfo,
            List<TourTimelineDto> timelines,
            Integer numberOfPeople,
            BigDecimal totalPrice,
            LocalDate startDate,
            LocalDate endDate) {

        try {
            log.info("Preparing booking confirmation email for user: {}", userInfo.getEmail());

            // Generate Google Calendar link
            String calendarLink = GoogleCalendarUtil.generateGoogleCalendarLink(
                    tourInfo.getName(),
                    buildCalendarDescription(tourInfo, timelines),
                    tourInfo.getLocation() != null ? tourInfo.getLocation() : "Vietnam",
                    startDate,
                    endDate);

            // Group timeline by day
            Map<Integer, List<TourTimelineDto>> timelineByDay = timelines.stream()
                    .collect(Collectors.groupingBy(TourTimelineDto::getDayNumber));

            // Prepare email context
            Context context = new Context();
            context.setVariable("fullName", userInfo.getFullName());
            context.setVariable("tourName", tourInfo.getName());
            context.setVariable("location", tourInfo.getLocation());
            context.setVariable("startDate", startDate);
            context.setVariable("endDate", endDate);
            context.setVariable("duration", tourInfo.getDurationDays());
            context.setVariable("numberOfPeople", numberOfPeople);
            context.setVariable("totalPrice", totalPrice);
            context.setVariable("calendarLink", calendarLink);

            // Prepare timeline for template
            List<Map<String, Object>> timelineData = timelineByDay.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> Map.<String, Object>of(
                            "dayNumber", entry.getKey(),
                            "activities", entry.getValue()))
                    .collect(Collectors.toList());
            context.setVariable("timeline", timelineData);

            // Process HTML template
            String htmlContent = templateEngine.process("booking-confirmation-email", context);

            // Send email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(userInfo.getEmail());
            helper.setSubject("🎉 Your Tour Booking is Confirmed - " + tourInfo.getName());
            helper.setText(htmlContent, true);

            mailSender.send(message);

            log.info("Booking confirmation email sent successfully to: {}", userInfo.getEmail());

        } catch (MessagingException e) {
            log.error("Failed to send booking confirmation email to: {}", userInfo.getEmail(), e);
            throw new RuntimeException("Failed to send confirmation email", e);
        }
    }

    /**
     * Build calendar description with tour details and timeline
     */
    private String buildCalendarDescription(TourInfoDto tourInfo, List<TourTimelineDto> timelines) {
        StringBuilder description = new StringBuilder();
        description.append(tourInfo.getName()).append("\\n\\n");

        if (tourInfo.getDescription() != null) {
            description.append(tourInfo.getDescription()).append("\\n\\n");
        }

        description.append("Tour Timeline:\\n");

        Map<Integer, List<TourTimelineDto>> timelineByDay = timelines.stream()
                .collect(Collectors.groupingBy(TourTimelineDto::getDayNumber));

        timelineByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    description.append("\\nDay ").append(entry.getKey()).append(":\\n");
                    entry.getValue().forEach(activity -> {
                        description.append("• ").append(activity.getTitle());
                        if (activity.getStartTime() != null && activity.getEndTime() != null) {
                            description.append(" (")
                                    .append(activity.getStartTime())
                                    .append(" - ")
                                    .append(activity.getEndTime())
                                    .append(")");
                        }
                        description.append("\\n");
                    });
                });

        return description.toString();
    }
}
