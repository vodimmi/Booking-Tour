# Quick Start Guide - Tour Booking Email Feature

## Prerequisites

- Docker & Docker Compose installed
- Gmail account (for email testing)
- All services in the Booking-Tour project

## Step-by-Step Setup

### Step 1: Configure Gmail App Password

1. **Go to Google Account Settings**
   - Visit: https://myaccount.google.com/security
2. **Enable 2-Step Verification** (if not already enabled)

   - Click on "2-Step Verification"
   - Follow the setup process

3. **Generate App Password**
   - Search for "App passwords" in settings
   - Select app: "Mail"
   - Select device: "Other (Custom name)" → Type "Booking Tour"
   - Click "Generate"
   - **Copy the 16-character password** (e.g., `abcd efgh ijkl mnop`)

### Step 2: Configure Environment Variables

Create a `.env` file in the project root:

```bash
# Create .env file
cat > .env << 'EOF'
# Email Configuration
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=abcd efgh ijkl mnop

# Service URLs (for Docker)
AUTH_SERVICE_URL=http://service-auth:6060
TOUR_SERVICE_URL=http://service-tour:8081
EOF
```

**Replace:**

- `your-email@gmail.com` with your actual Gmail address
- `abcd efgh ijkl mnop` with your generated app password

### Step 3: Update docker-compose.yml

Add environment variables to `service-booking`:

```yaml
service-booking:
  build:
    context: .
    dockerfile: service-booking/Dockerfile
    network: host
  container_name: bt-service-booking
  environment:
    # ... existing environment variables ...

    # Email Configuration
    MAIL_USERNAME: ${MAIL_USERNAME}
    MAIL_PASSWORD: ${MAIL_PASSWORD}

    # Service URLs
    AUTH_SERVICE_URL: http://service-auth:6060
    TOUR_SERVICE_URL: http://service-tour:8081
  # ... rest of configuration ...
```

### Step 4: Build and Start Services

```bash
# Stop existing containers
docker-compose down

# Build and start all services
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build
```

### Step 5: Wait for Services to Start

Monitor logs to ensure all services are running:

```bash
# Watch all logs
docker-compose logs -f

# Or watch specific service
docker logs bt-service-booking -f
docker logs bt-service-tour -f
docker logs bt-mysql -f
```

Look for:

```
✅ service-tour: Started TourServiceApplication
✅ service-booking: Started BookingApplication
✅ service-auth: Started AuthApplication
✅ mysql: ready for connections
```

### Step 6: Verify Database Migration

Check if timeline table was created:

```bash
# Connect to MySQL
docker exec -it bt-mysql mysql -uroot -proot

# Check tables
USE BT_TOUR;
SHOW TABLES;
# Should show: tour_timelines

# Check data
SELECT * FROM tour_timelines;
# Should show sample timeline data

# Exit MySQL
exit
```

### Step 7: Test Timeline API

```bash
# Test tour timeline endpoint
curl http://localhost:8081/api/tours/1/timeline

# Expected output (abbreviated):
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

### Step 8: Test Complete Flow

#### A. Create a Booking

```bash
curl -X POST http://localhost:8082/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "tourId": 1,
    "numberOfPeople": 2,
    "totalPrice": 5000000,
    "bookingDate": "2024-12-04T10:00:00",
    "tourStartDate": "2024-12-15",
    "tourEndDate": "2024-12-17",
    "specialRequirements": "None"
  }'
```

**Save the booking ID** from the response (e.g., `{"id": 123}`)

#### B. Confirm the Booking (Triggers Email)

```bash
# Replace {bookingId} with actual ID from previous step
curl -X PUT http://localhost:8082/api/bookings/{bookingId}/confirm

# Example:
curl -X PUT http://localhost:8082/api/bookings/1/confirm
```

### Step 9: Verify Email Sent

#### Check Application Logs:

```bash
docker logs bt-service-booking -f
```

Look for:

```
✅ Sending booking confirmation email for bookingId: 1
✅ Fetching user info from: http://service-auth:6060/api/users/1
✅ Fetching tour info from: http://service-tour:8081/api/tours/1
✅ Fetching tour timeline from: http://service-tour:8081/api/tours/1/timeline
✅ Booking confirmation email sent successfully for bookingId: 1
```

#### Check Your Email Inbox:

1. Open your Gmail inbox
2. Look for email: **"🎉 Your Tour Booking is Confirmed - Tour Phú Quốc 3N2Đ"**
3. Email should contain:
   - Your name
   - Tour details
   - Complete timeline
   - "Add to Google Calendar" button

### Step 10: Test Google Calendar Integration

1. Open the email
2. Click **"📆 Add to Google Calendar"** button
3. Google Calendar should open with pre-filled event:
   - Title: Tour name
   - Dates: Tour start/end dates
   - Location: Tour location
   - Description: Complete timeline
4. Click "Save" to add to your calendar

## Troubleshooting

### Issue: Email Not Sending

**Symptoms:**

- Booking confirmed but no email received
- Logs show email errors

**Solutions:**

1. **Check Gmail App Password:**
   ```bash
   # View environment variables
   docker exec bt-service-booking printenv | grep MAIL
   ```
2. **Verify SMTP Settings:**

   ```bash
   # Test SMTP connection
   telnet smtp.gmail.com 587
   ```

3. **Check Gmail "Less secure app" settings:**

   - Ensure 2-Step Verification is enabled
   - Ensure App Password is correct (no spaces)

4. **Check spam folder**

### Issue: Service Communication Fails

**Symptoms:**

- Logs show "Connection refused" or "Service not found"

**Solutions:**

1. **Verify Service URLs:**

   ```bash
   # Check environment variables
   docker exec bt-service-booking printenv | grep SERVICE_URL

   # Should show:
   # AUTH_SERVICE_URL=http://service-auth:6060
   # TOUR_SERVICE_URL=http://service-tour:8081
   ```

2. **Test Service Connectivity:**

   ```bash
   # From booking service, ping other services
   docker exec bt-service-booking curl http://service-auth:6060/api/users/1
   docker exec bt-service-booking curl http://service-tour:8081/api/tours/1
   ```

3. **Check all services are running:**
   ```bash
   docker ps
   # Should show: bt-service-auth, bt-service-tour, bt-service-booking
   ```

### Issue: Timeline Data Missing

**Symptoms:**

- Email sent but no timeline in email
- Timeline API returns empty array

**Solutions:**

1. **Check migration ran:**

   ```bash
   docker exec -it bt-mysql mysql -uroot -proot -e "USE BT_TOUR; SELECT COUNT(*) FROM tour_timelines;"
   ```

2. **Re-run migration:**

   ```bash
   docker-compose down
   docker-compose up --build
   ```

3. **Manually insert data:**
   ```bash
   docker exec -it bt-mysql mysql -uroot -proot BT_TOUR < service-tour/src/main/resources/db/migration/V2__add_tour_timelines.sql
   ```

### Issue: HTML Email Not Rendering

**Symptoms:**

- Email sent but shows plain text or broken HTML

**Solutions:**

1. **Check template file exists:**

   ```bash
   ls -la service-booking/src/main/resources/templates/booking-confirmation-email.html
   ```

2. **View logs for template errors:**

   ```bash
   docker logs bt-service-booking | grep -i thymeleaf
   ```

3. **Rebuild service:**
   ```bash
   docker-compose up --build service-booking
   ```

## Verification Checklist

Before considering setup complete:

- [ ] `.env` file created with Gmail credentials
- [ ] docker-compose.yml updated with environment variables
- [ ] All services built and running (`docker ps`)
- [ ] MySQL database contains `tour_timelines` table
- [ ] Timeline API returns data: `curl localhost:8081/api/tours/1/timeline`
- [ ] Booking created successfully
- [ ] Booking confirmed successfully
- [ ] Logs show "email sent successfully"
- [ ] Email received in inbox
- [ ] Email displays correctly with timeline
- [ ] Google Calendar button works

## Quick Commands Reference

```bash
# Start services
docker-compose up -d

# View logs
docker logs bt-service-booking -f

# Stop services
docker-compose down

# Rebuild specific service
docker-compose up --build service-booking

# Check MySQL data
docker exec -it bt-mysql mysql -uroot -proot -e "USE BT_TOUR; SELECT * FROM tour_timelines;"

# Test timeline API
curl http://localhost:8081/api/tours/1/timeline

# Create booking
curl -X POST http://localhost:8082/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"tourId":1,"numberOfPeople":2,"totalPrice":5000000,"bookingDate":"2024-12-04T10:00:00","tourStartDate":"2024-12-15","tourEndDate":"2024-12-17"}'

# Confirm booking (replace {id})
curl -X PUT http://localhost:8082/api/bookings/{id}/confirm
```

## Next Steps

1. **Customize Email Template**

   - Edit: `service-booking/src/main/resources/templates/booking-confirmation-email.html`
   - Add your branding, colors, logo

2. **Add More Timeline Data**

   - Create timeline entries for other tours in your database

3. **Production Setup**

   - Use professional SMTP service (SendGrid, Mailgun)
   - Configure proper domain for emails
   - Set up email monitoring

4. **Additional Features**
   - Add .ics file attachment
   - Send reminder emails 1 day before tour
   - Add cancellation email
   - Multi-language support

## Support

If you encounter issues:

1. Check logs: `docker logs bt-service-booking`
2. Verify configuration in `application.yml`
3. Test each component individually
4. Review implementation guide: `TASK003_IMPLEMENTATION_GUIDE.md`

---

**Setup Time**: ~15-20 minutes  
**Difficulty**: Intermediate  
**Last Updated**: December 4, 2024
