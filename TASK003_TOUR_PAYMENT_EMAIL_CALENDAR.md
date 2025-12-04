# TASK003_TOUR_PAYMENT_EMAIL_CALENDAR.md

## Task Title

Implement “Tour Payment Success Email + Google Calendar Integration”

---

## Objective

When a user successfully pays for a booking (booking status changes to `CONFIRMED`), the system must automatically:

1. Send a confirmation email containing:
   - Tour details (name, dates, price, location)
   - Full tour timeline (activities per day)
2. Include a **Google Calendar Add Link**
3. _(Optional)_ Attach a `.ics` calendar file

This feature improves user experience by helping users easily remember their upcoming tour schedule.

---

## Affected Services

- `service-booking`
- `service-tour`
- `service-auth` (fetch user email)
- `service-notification` _(if exists, else use local EmailService)_

---

## Requirements

### 1. Extend Tour Service — Add Timeline Support

#### New Database Table: `tour_timelines`

| Field       | Type      | Description      |
| ----------- | --------- | ---------------- |
| id          | BIGINT PK |                  |
| tour_id     | BIGINT FK | Parent tour      |
| day_number  | INT       | Day index        |
| title       | VARCHAR   | Activity title   |
| description | TEXT      | Activity details |
| start_time  | TIME      | Start time       |
| end_time    | TIME      | End time         |

#### New API Endpoint

GET /api/tours/{id}/timeline

---

### 2. Modify Booking Confirmation Flow

Inside `service-booking`:

When booking changes from `PENDING → CONFIRMED`:

1. Fetch user info from `service-auth`
2. Fetch tour info from `service-tour`
3. Fetch timeline from `service-tour`
4. Generate Google Calendar link
5. Send email to user

**Entry point:**  
`BookingService.confirmBooking()` or payment webhook handler.

---

### 3. Email Sending Logic

Use Spring Boot `JavaMailSender`.

Email must include:

- Greeting (`fullName`)
- Tour name
- Start & end dates
- Timeline activities (loop by day)
- Button or link: **Add to Google Calendar**

Email template file:
