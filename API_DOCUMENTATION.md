# 📚 API Documentation Summary

## Quick Links

- **[Complete API Flow with cURL](./API_CURL_COMPLETE_FLOW.md)** - Comprehensive guide with all endpoints
- **[Quick Test Commands](./QUICK_TEST_COMMANDS.md)** - Copy-paste ready commands for rapid testing
- **[Implementation Guide](./TASK003_IMPLEMENTATION_GUIDE.md)** - Technical implementation details
- **[Quick Start](./TASK003_QUICK_START.md)** - Step-by-step setup guide

---

## 🎯 API Overview

### Services & Ports

| Service             | Port | Description                      | Base URL              |
| ------------------- | ---- | -------------------------------- | --------------------- |
| **service-auth**    | 6060 | Authentication & User Management | http://localhost:6060 |
| **service-tour**    | 8081 | Tour Management & Timeline       | http://localhost:8081 |
| **service-booking** | 8082 | Booking Management & Email       | http://localhost:8082 |
| **MySQL**           | 3307 | Database                         | localhost:3307        |

---

## 🔑 Authentication

All protected endpoints require Bearer token authentication:

```bash
Authorization: Bearer eyJhbGciOiJSUzI1NiJ9...
```

### Get Token

```bash
curl -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"User@123"}'
```

### Default Accounts

| Role  | Email             | Password  |
| ----- | ----------------- | --------- |
| Admin | admin@example.com | Admin@123 |
| User  | user@example.com  | User@123  |

---

## 📍 Core Endpoints

### Authentication Service (Port 6060)

| Method | Endpoint                    | Description              | Auth Required |
| ------ | --------------------------- | ------------------------ | ------------- |
| POST   | `/api/auth/register`        | Register new user        | ❌            |
| POST   | `/api/auth/login`           | Login                    | ❌            |
| POST   | `/api/auth/refresh`         | Refresh token            | ❌            |
| GET    | `/api/auth/me`              | Get current user profile | ✅            |
| PUT    | `/api/auth/me`              | Update profile           | ✅            |
| POST   | `/api/auth/change-password` | Change password          | ✅            |
| POST   | `/api/auth/logout`          | Logout                   | ✅            |
| GET    | `/api/users`                | List all users           | ✅ (Admin)    |
| GET    | `/api/users/{id}`           | Get user by ID           | ✅            |

### Tour Service (Port 8081)

| Method | Endpoint                           | Description                | Auth Required |
| ------ | ---------------------------------- | -------------------------- | ------------- |
| GET    | `/api/tours`                       | List all tours (paginated) | ❌            |
| GET    | `/api/tours/{id}`                  | Get tour by ID             | ❌            |
| GET    | `/api/tours/search?q={keyword}`    | Search tours               | ❌            |
| GET    | `/api/tours/category/{categoryId}` | Get tours by category      | ❌            |
| GET    | `/api/tours/{id}/timeline`         | **Get tour timeline** 🆕   | ❌            |
| POST   | `/api/tours`                       | Create new tour            | ✅ (Admin)    |
| PUT    | `/api/tours/{id}`                  | Update tour                | ✅ (Admin)    |
| DELETE | `/api/tours/{id}`                  | Delete tour                | ✅ (Admin)    |
| GET    | `/api/tour-categories`             | List categories            | ❌            |
| GET    | `/api/locations`                   | List locations             | ❌            |

### Booking Service (Port 8082)

| Method | Endpoint                      | Description            | Auth Required |
| ------ | ----------------------------- | ---------------------- | ------------- |
| POST   | `/api/bookings`               | Create booking         | ✅            |
| GET    | `/api/bookings`               | List all bookings      | ✅            |
| GET    | `/api/bookings/{id}`          | Get booking by ID      | ✅            |
| GET    | `/api/bookings/user/{userId}` | Get user's bookings    | ✅            |
| PUT    | `/api/bookings/{id}/confirm`  | **Confirm booking** 🎉 | ✅ (Admin)    |
| PUT    | `/api/bookings/{id}/cancel`   | Cancel booking         | ✅            |
| PUT    | `/api/bookings/{id}/reject`   | Reject booking         | ✅ (Admin)    |

---

## 🆕 New Feature: Email Notification

### Trigger: Booking Confirmation

When an admin confirms a booking via `PUT /api/bookings/{id}/confirm`, the system:

1. ✅ Changes booking status to `CONFIRMED`
2. ✅ Fetches user information from auth service
3. ✅ Fetches tour details from tour service
4. ✅ Fetches tour timeline from tour service
5. ✅ Generates Google Calendar link
6. ✅ Sends HTML email with:
   - Complete tour information
   - Day-by-day timeline with activities
   - "Add to Google Calendar" button

### Email Content Preview

**Subject**: 🎉 Your Tour Booking is Confirmed - [Tour Name]

**Contains**:

- Personalized greeting
- Tour details (name, location, dates, price)
- Complete timeline grouped by day
- Activity details with time slots
- Google Calendar integration link

---

## 📊 Response Examples

### Login Response

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJSUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 1800,
  "user": {
    "id": 2,
    "email": "user@example.com",
    "fullName": "Regular User",
    "avatarUrl": null,
    "roles": ["USER"]
  }
}
```

### Tour Response

```json
{
  "id": 1,
  "name": "Tour Phú Quốc 3N2Đ",
  "description": "Nghỉ dưỡng và lặn biển",
  "price": 2500000.0,
  "duration": 3,
  "category": "Du lịch biển",
  "location": "Phú Quốc",
  "startDate": "2024-12-15",
  "endDate": "2024-12-17",
  "maxParticipants": 30,
  "currentParticipants": 25,
  "rating": 4.5,
  "reviews": 120
}
```

### Timeline Response 🆕

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
  },
  {
    "id": 2,
    "tourId": 1,
    "dayNumber": 1,
    "title": "Tham quan chợ đêm",
    "description": "Khám phá ẩm thực địa phương tại chợ đêm Phú Quốc",
    "startTime": "18:00:00",
    "endTime": "21:00:00"
  }
]
```

### Booking Response

```json
{
  "id": 1,
  "userId": 2,
  "tourId": 1,
  "numberOfPeople": 2,
  "totalPrice": 5000000.0,
  "bookingDate": "2024-12-04T10:30:00",
  "tourStartDate": "2024-12-15",
  "tourEndDate": "2024-12-17",
  "status": "CONFIRMED",
  "specialRequirements": "Vegetarian meals",
  "createdAt": "2024-12-04T10:30:00",
  "updatedAt": "2024-12-04T11:00:00"
}
```

---

## 🔄 Complete User Flow

```
1. User Registration
   POST /api/auth/register

2. User Login
   POST /api/auth/login
   → Receive access token

3. Browse Tours
   GET /api/tours?page=1&limit=10

4. View Tour Details
   GET /api/tours/1

5. View Tour Timeline 🆕
   GET /api/tours/1/timeline

6. Create Booking
   POST /api/bookings
   → Status: PENDING

7. Admin Confirms Booking
   PUT /api/bookings/1/confirm
   → Status: CONFIRMED
   → Email sent automatically 📧

8. User Receives Email
   ✉️ HTML email with timeline
   📅 Google Calendar link
```

---

## 🧪 Quick Test (2 Minutes)

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"User@123"}' \
  | jq -r '.accessToken')

# 2. View tours
curl -s http://localhost:8081/api/tours | jq '.[0]'

# 3. View timeline
curl -s http://localhost:8081/api/tours/1/timeline | jq

# 4. Create booking
curl -X POST http://localhost:8082/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"userId":2,"tourId":1,"numberOfPeople":2,"totalPrice":5000000,"bookingDate":"2024-12-04T10:00:00","tourStartDate":"2024-12-15","tourEndDate":"2024-12-17"}' -i

# 5. Confirm (as admin)
ADMIN_TOKEN=$(curl -s -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"Admin@123"}' \
  | jq -r '.accessToken')

curl -X PUT http://localhost:8082/api/bookings/1/confirm \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

---

## 🐛 Common Issues

### 401 Unauthorized

- Token expired (valid for 30 minutes)
- Token not included in header
- **Solution**: Login again to get new token

### 404 Not Found

- Invalid ID
- Resource doesn't exist
- **Solution**: Check if resource exists first

### 500 Internal Server Error

- Service down
- Database connection issue
- **Solution**: Check logs with `docker logs [service-name]`

---

## 📞 Support Commands

```bash
# Check services status
docker ps

# View logs
docker logs bt-service-booking -f
docker logs bt-service-tour -f
docker logs bt-service-auth -f

# Restart service
docker-compose restart service-booking

# Rebuild service
docker-compose up -d --build service-booking

# Check database
docker exec -it bt-mysql mysql -uroot -proot
```

---

## 📖 Additional Resources

- **Postman Collection**: `postman/collections/Booking Tour - Booking Service API.postman_collection.json`
- **Swagger UI**: http://localhost:6060/swagger-ui.html (when enabled)
- **OpenAPI Docs**: http://localhost:6060/v3/api-docs

---

**Version**: 1.0.0  
**Last Updated**: December 4, 2024  
**Feature**: Email Notification with Tour Timeline ✨
