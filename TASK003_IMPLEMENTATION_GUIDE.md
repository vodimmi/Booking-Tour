# Tour Payment Success Email + Google Calendar Integration - Implementation Guide

## Overview

This feature automatically sends a confirmation email to users when their tour booking payment is successful. The email includes:

- Complete tour details (name, dates, location, price)
- Full tour timeline with day-by-day activities
- Google Calendar integration link
- Professional HTML email template

## Implementation Summary

### 1. Service-Tour Enhancements

#### New Database Table: `tour_timelines`

```sql
CREATE TABLE tour_timelines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tour_id BIGINT NOT NULL,
    day_number INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time TIME,
    end_time TIME,
    FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE CASCADE
);
```

#### New Files Created:

- **Entity**: `service-tour/src/main/java/com/example/tour/entity/TourTimeline.java`
- **Repository**: `service-tour/src/main/java/com/example/tour/repository/TourTimelineRepository.java`
- **Service**: `service-tour/src/main/java/com/example/tour/service/TourTimelineService.java`
- **DTO**: `service-tour/src/main/java/com/example/tour/dto/TourTimelineDto.java`
- **Migration**: `service-tour/src/main/resources/db/migration/V2__add_tour_timelines.sql`

#### New API Endpoint:

```
GET /api/tours/{id}/timeline
```

Returns the complete timeline for a specific tour, ordered by day number and start time.

### 2. Service-Booking Enhancements

#### New Dependencies Added:

- `spring-boot-starter-mail` - For sending emails
- `spring-boot-starter-thymeleaf` - For HTML email templates

#### New Files Created:

- **Email Service**: `service-booking/src/main/java/com/example/booking/application/service/EmailService.java`
- **External Service Client**: `service-booking/src/main/java/com/example/booking/application/service/ExternalServiceClient.java`
- **Google Calendar Utility**: `service-booking/src/main/java/com/example/booking/application/util/GoogleCalendarUtil.java`
- **Email Template**: `service-booking/src/main/resources/templates/booking-confirmation-email.html`
- **RestTemplate Config**: `service-booking/src/main/java/com/example/booking/config/RestTemplateConfig.java`

#### DTOs for Inter-Service Communication:

- `UserInfoDto.java` - For fetching user details from auth service
- `TourInfoDto.java` - For fetching tour details from tour service
- `TourTimelineDto.java` - For fetching timeline from tour service

#### Modified Files:

- `BookingCommandService.java` - Enhanced `handleConfirmBooking()` method
- `application.yml` - Added email and service URL configurations
- `pom.xml` - Added mail and thymeleaf dependencies

## Configuration

### 1. Email Configuration (service-booking/application.yml)

For **Gmail** (recommended for testing):

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password # Use App Password, not regular password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

**How to get Gmail App Password:**

1. Go to Google Account Settings
2. Security > 2-Step Verification (enable if not already)
3. Search for "App passwords"
4. Generate new app password for "Mail"
5. Use that 16-character password in configuration

### 2. Environment Variables

You can configure via environment variables:

```bash
# Email configuration
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password

# Service URLs (for Docker)
export AUTH_SERVICE_URL=http://service-auth:6060
export TOUR_SERVICE_URL=http://service-tour:8081
```

### 3. Docker Compose Configuration

Update `docker-compose.yml` for service-booking:

```yaml
service-booking:
  environment:
    # ... existing env vars ...
    MAIL_USERNAME: ${MAIL_USERNAME}
    MAIL_PASSWORD: ${MAIL_PASSWORD}
    AUTH_SERVICE_URL: http://service-auth:6060
    TOUR_SERVICE_URL: http://service-tour:8081
```

## How It Works

### Flow Diagram:

```
1. User Payment Success
   ↓
2. Booking Status → CONFIRMED
   ↓
3. BookingCommandService.handleConfirmBooking()
   ↓
4. Fetch User Info (from service-auth)
   ↓
5. Fetch Tour Info (from service-tour)
   ↓
6. Fetch Tour Timeline (from service-tour)
   ↓
7. Generate Google Calendar Link
   ↓
8. Send HTML Email with Timeline
   ↓
9. User Receives Email
```

### Key Components:

1. **ExternalServiceClient**: Makes REST calls to other microservices

   - `getUserInfo(userId)` → Calls auth service
   - `getTourInfo(tourId)` → Calls tour service
   - `getTourTimeline(tourId)` → Calls tour service

2. **GoogleCalendarUtil**: Generates calendar links and .ics files

   - Creates properly formatted Google Calendar URLs
   - Generates RFC-5545 compliant .ics content

3. **EmailService**: Sends templated HTML emails

   - Uses Thymeleaf for template processing
   - Includes tour details and timeline
   - Embeds Google Calendar link

4. **Email Template**: Professional HTML design
   - Responsive layout
   - Timeline grouped by day
   - Activity cards with time information
   - Calendar button/link

## Testing

### 1. Test Timeline API

```bash
# Get timeline for tour ID 1
curl http://localhost:8081/api/tours/1/timeline
```

Expected response:

```json
[
  {
    "id": 1,
    "tourId": 1,
    "dayNumber": 1,
    "title": "Đón khách tại sân bay",
    "description": "Xe đón khách tại sân bay Phú Quốc...",
    "startTime": "08:00:00",
    "endTime": "10:00:00"
  },
  ...
]
```

### 2. Test Booking Confirmation

```bash
# Create a booking
curl -X POST http://localhost:8082/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "tourId": 1,
    "numberOfPeople": 2,
    "totalPrice": 5000000,
    "tourStartDate": "2024-12-15",
    "tourEndDate": "2024-12-17"
  }'

# Confirm the booking (triggers email)
curl -X PUT http://localhost:8082/api/bookings/{bookingId}/confirm
```

### 3. Test Email Locally

For development, you can use:

- **Mailtrap.io** - Free email testing service
- **Ethereal Email** - Temporary email testing
- **Gmail** with App Password

### 4. Check Logs

```bash
docker logs bt-service-booking -f
```

Look for:

```
Sending booking confirmation email for bookingId: 1
Fetching user info from: http://service-auth:6060/api/users/1
Fetching tour info from: http://service-tour:8081/api/tours/1
Fetching tour timeline from: http://service-tour:8081/api/tours/1/timeline
Booking confirmation email sent successfully for bookingId: 1
```

## Sample Email Content

The email will include:

**Header**: "🎉 Your Tour Booking is Confirmed!"

**Tour Details**:

- Tour Name: Tour Phú Quốc 3N2Đ
- Location: Phú Quốc
- Start Date: 15/12/2024
- End Date: 17/12/2024
- Duration: 3 days
- Participants: 2
- Total Price: 5,000,000 VND

**Timeline**:

- **Day 1**
  - 08:00 - 10:00: Đón khách tại sân bay
  - 18:00 - 21:00: Tham quan chợ đêm
- **Day 2**
  - 08:00 - 16:00: Tour 4 đảo
  - ...

**Action Button**: "📆 Add to Google Calendar"

## Troubleshooting

### Email Not Sending

1. **Check email credentials**:

   ```bash
   # View service-booking logs
   docker logs bt-service-booking
   ```

2. **Verify SMTP settings**:

   - Gmail requires App Password (not regular password)
   - Check firewall allows port 587

3. **Test SMTP connection**:
   ```bash
   telnet smtp.gmail.com 587
   ```

### Inter-Service Communication Issues

1. **Check service URLs**:

   ```yaml
   # In docker-compose, services communicate via service name
   AUTH_SERVICE_URL: http://service-auth:6060
   TOUR_SERVICE_URL: http://service-tour:8081
   ```

2. **Verify services are running**:
   ```bash
   docker ps
   curl http://localhost:6060/api/users/1
   curl http://localhost:8081/api/tours/1
   ```

### Timeline Not Showing

1. **Check migration ran successfully**:

   ```bash
   docker exec -it bt-mysql mysql -uroot -proot -e "USE BT_TOUR; SHOW TABLES;"
   docker exec -it bt-mysql mysql -uroot -proot -e "USE BT_TOUR; SELECT * FROM tour_timelines;"
   ```

2. **Verify timeline data exists**:
   ```bash
   curl http://localhost:8081/api/tours/1/timeline
   ```

## Database Schema Changes

### New Table: tour_timelines

| Column      | Type         | Description                  |
| ----------- | ------------ | ---------------------------- |
| id          | BIGINT       | Primary key                  |
| tour_id     | BIGINT       | Foreign key to tours table   |
| day_number  | INT          | Day of the tour (1, 2, 3...) |
| title       | VARCHAR(255) | Activity title               |
| description | TEXT         | Activity description         |
| start_time  | TIME         | Activity start time          |
| end_time    | TIME         | Activity end time            |

Sample data has been seeded for tours 1 and 2.

## API Documentation

### Get Tour Timeline

**Endpoint**: `GET /api/tours/{id}/timeline`

**Response**:

```json
[
  {
    "id": 1,
    "tourId": 1,
    "dayNumber": 1,
    "title": "Đón khách tại sân bay",
    "description": "Xe đón khách tại sân bay Phú Quốc, di chuyển về khách sạn check-in",
    "startTime": "08:00:00",
    "endTime": "10:00:00"
  }
]
```

## Future Enhancements

Potential improvements:

1. **Add .ics file attachment** to email
2. **SMS notifications** for booking confirmation
3. **Multi-language support** for emails
4. **Email tracking** (open rate, click rate)
5. **Outlook Calendar** integration
6. **Email templates** for different events (cancellation, reminder)
7. **Scheduled reminders** (1 day before tour)

## Production Checklist

Before deploying to production:

- [ ] Configure production SMTP server (not Gmail)
- [ ] Set up proper email credentials via secrets
- [ ] Test email delivery
- [ ] Configure proper service URLs
- [ ] Test inter-service communication
- [ ] Verify database migrations
- [ ] Add monitoring for email failures
- [ ] Set up email retry mechanism
- [ ] Configure rate limiting for emails
- [ ] Test with real user data
- [ ] Verify Google Calendar links work
- [ ] Check email rendering in different clients

## Support

For issues or questions:

- Check application logs: `docker logs bt-service-booking`
- Review email configuration in `application.yml`
- Verify all services are running: `docker ps`
- Test individual components separately

---

**Implementation Date**: December 2024
**Author**: Development Team
**Version**: 1.0.0
