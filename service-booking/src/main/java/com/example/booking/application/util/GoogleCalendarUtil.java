package com.example.booking.application.util;

import lombok.experimental.UtilityClass;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class GoogleCalendarUtil {

    private static final DateTimeFormatter GOOGLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    private static final DateTimeFormatter ICS_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    /**
     * Generate Google Calendar add event URL
     * 
     * @param title       Event title
     * @param description Event description
     * @param location    Event location
     * @param startDate   Start date
     * @param endDate     End date
     * @return Google Calendar URL
     */
    public static String generateGoogleCalendarLink(
            String title,
            String description,
            String location,
            LocalDate startDate,
            LocalDate endDate) {

        try {
            // Convert LocalDate to LocalDateTime (start of day)
            LocalDateTime startDateTime = startDate.atTime(LocalTime.of(8, 0));
            LocalDateTime endDateTime = endDate.atTime(LocalTime.of(18, 0));

            // Format dates for Google Calendar (UTC)
            String startFormatted = startDateTime.atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC"))
                    .format(GOOGLE_DATE_FORMAT);
            String endFormatted = endDateTime.atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC"))
                    .format(GOOGLE_DATE_FORMAT);

            // Build Google Calendar URL
            StringBuilder url = new StringBuilder("https://calendar.google.com/calendar/render?action=TEMPLATE");
            url.append("&text=").append(urlEncode(title));
            url.append("&dates=").append(startFormatted).append("/").append(endFormatted);
            url.append("&details=").append(urlEncode(description));
            url.append("&location=").append(urlEncode(location));

            return url.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Google Calendar link", e);
        }
    }

    /**
     * Generate .ics calendar file content
     * 
     * @param title       Event title
     * @param description Event description
     * @param location    Event location
     * @param startDate   Start date
     * @param endDate     End date
     * @return ICS file content
     */
    public static String generateIcsContent(
            String title,
            String description,
            String location,
            LocalDate startDate,
            LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atTime(LocalTime.of(8, 0));
        LocalDateTime endDateTime = endDate.atTime(LocalTime.of(18, 0));

        String startFormatted = startDateTime.format(ICS_DATE_FORMAT);
        String endFormatted = endDateTime.format(ICS_DATE_FORMAT);
        String timestamp = LocalDateTime.now().format(ICS_DATE_FORMAT);

        StringBuilder ics = new StringBuilder();
        ics.append("BEGIN:VCALENDAR\n");
        ics.append("VERSION:2.0\n");
        ics.append("PRODID:-//Booking Tour//Tour Booking//EN\n");
        ics.append("METHOD:REQUEST\n");
        ics.append("BEGIN:VEVENT\n");
        ics.append("UID:").append(System.currentTimeMillis()).append("@bookingtour.com\n");
        ics.append("DTSTAMP:").append(timestamp).append("\n");
        ics.append("DTSTART:").append(startFormatted).append("\n");
        ics.append("DTEND:").append(endFormatted).append("\n");
        ics.append("SUMMARY:").append(escapeIcsText(title)).append("\n");
        ics.append("DESCRIPTION:").append(escapeIcsText(description)).append("\n");
        ics.append("LOCATION:").append(escapeIcsText(location)).append("\n");
        ics.append("STATUS:CONFIRMED\n");
        ics.append("SEQUENCE:0\n");
        ics.append("BEGIN:VALARM\n");
        ics.append("TRIGGER:-P1D\n");
        ics.append("DESCRIPTION:Reminder: Your tour starts tomorrow\n");
        ics.append("ACTION:DISPLAY\n");
        ics.append("END:VALARM\n");
        ics.append("END:VEVENT\n");
        ics.append("END:VCALENDAR\n");

        return ics.toString();
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }

    private static String escapeIcsText(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\n", "\\n")
                .replace(",", "\\,")
                .replace(";", "\\;");
    }
}
