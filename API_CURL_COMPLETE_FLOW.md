# Complete API Flow - cURL Commands

This document provides a comprehensive list of cURL commands to test the entire Booking-Tour application flow, including the new email notification feature.

## Prerequisites

- All services running: `docker-compose up -d`
- Services available at:
  - **service-auth**: http://localhost:6060
  - **service-tour**: http://localhost:8081
  - **service-booking**: http://localhost:8082

---

## 📋 Full Flow Test Scenario

### **Scenario**: A user registers, logs in, browses tours, creates a booking, and receives a confirmation email

---

## 1️⃣ **AUTHENTICATION SERVICE** (Port 6060)

### 1.1 Register a New User

```bash
curl -X POST http://localhost:6060/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass@123",
    "fullName": "John Doe"
  }'
```

**Expected Response:**

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJSUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 1800,
  "user": {
    "id": 3,
    "email": "john.doe@example.com",
    "fullName": "John Doe"
  }
}
```

### 1.2 Login with Existing User

```bash
curl -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "User@123"
  }'
```

**Expected Response:**

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJSUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 1800,
  "user": {
    "id": 2,
    "email": "user@example.com",
    "fullName": "Regular User"
  }
}
```

**💡 Save the `accessToken` for subsequent requests!**

```bash
# Export token as variable for convenience
export ACCESS_TOKEN="eyJhbGciOiJSUzI1NiJ9..."
```

### 1.3 Get Current User Profile

```bash
curl -X GET http://localhost:6060/api/auth/me \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

### 1.4 Update User Profile

```bash
curl -X PUT http://localhost:6060/api/auth/me \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe Updated",
    "avatarUrl": "https://example.com/avatar.jpg"
  }'
```

### 1.5 Change Password

```bash
curl -X POST http://localhost:6060/api/auth/change-password \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "oldPassword": "User@123",
    "newPassword": "NewSecurePass@123"
  }'
```

### 1.6 Refresh Token

```bash
curl -X POST http://localhost:6060/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```

### 1.7 Logout

```bash
curl -X POST http://localhost:6060/api/auth/logout \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```

### 1.8 Get User by ID (For testing)

```bash
curl -X GET http://localhost:6060/api/users/2 \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

### 1.9 List All Users (Admin Only)

```bash
# Login as admin first
curl -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "Admin@123"
  }'

# Export admin token
export ADMIN_TOKEN="eyJhbGciOiJSUzI1NiJ9..."

# List users
curl -X GET "http://localhost:6060/api/users?page=0&size=10&sort=createdAt" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

---

## 2️⃣ **TOUR SERVICE** (Port 8081)

### 2.1 Get All Tours (Paginated)

```bash
curl -X GET "http://localhost:8081/api/tours?page=1&limit=10"
```

**Expected Response:**

```json
[
  {
    "id": 1,
    "name": "Tour Phú Quốc 3N2Đ",
    "description": "Nghỉ dưỡng và lặn biển",
    "price": 2500000.0,
    "duration": 3,
    "category": "Du lịch biển",
    "image": null,
    "rating": 0.0,
    "reviews": 0,
    "location": "Phú Quốc",
    "startDate": null,
    "endDate": null,
    "maxParticipants": 30,
    "currentParticipants": 25
  },
  {
    "id": 2,
    "name": "Tour Sa Pa 2N1Đ",
    "description": "Leo núi và săn mây",
    "price": 1500000.0,
    "duration": 2,
    "category": "Leo núi",
    ...
  }
]
```

### 2.2 Get Tour by ID

```bash
curl -X GET http://localhost:8081/api/tours/1
```

### 2.3 Search Tours by Keyword

```bash
curl -X GET "http://localhost:8081/api/tours/search?q=Phú Quốc&page=1&limit=10"
```

### 2.4 Get Tours by Category

```bash
curl -X GET "http://localhost:8081/api/tours/category/1?page=1&limit=10"
```

### 2.5 🆕 Get Tour Timeline (NEW FEATURE!)

```bash
curl -X GET http://localhost:8081/api/tours/1/timeline
```

**Expected Response:**

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
  },
  {
    "id": 3,
    "tourId": 1,
    "dayNumber": 2,
    "title": "Tour 4 đảo",
    "description": "Lặn ngắm san hô, câu cá, thưởng thức hải sản tươi sống",
    "startTime": "08:00:00",
    "endTime": "16:00:00"
  }
  // ... more timeline activities
]
```

### 2.6 Get All Tour Categories

```bash
curl -X GET http://localhost:8081/api/tour-categories
```

### 2.7 Get All Locations

```bash
curl -X GET http://localhost:8081/api/locations
```

### 2.8 Create a New Tour (Requires Authentication)

```bash
curl -X POST http://localhost:8081/api/tours \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tour Đà Lạt 4N3Đ",
    "description": "Khám phá thành phố ngàn hoa",
    "price": 3500000,
    "image": "dalat.jpg",
    "rating": 4.5,
    "reviewCount": 10,
    "startDate": "2024-12-20",
    "endDate": "2024-12-23",
    "durationDays": 4,
    "maxPeople": 25,
    "availableSlots": 20,
    "categoryId": 1,
    "locationId": 1
  }'
```

### 2.9 Update Tour

```bash
curl -X PUT http://localhost:8081/api/tours/1 \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tour Phú Quốc 3N2Đ - Updated",
    "price": 2700000,
    "availableSlots": 20
  }'
```

### 2.10 Delete Tour (Soft Delete)

```bash
curl -X DELETE http://localhost:8081/api/tours/1 \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

---

## 3️⃣ **BOOKING SERVICE** (Port 8082)

### 3.1 Create a New Booking

```bash
curl -X POST http://localhost:8082/api/bookings \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "tourId": 1,
    "numberOfPeople": 2,
    "totalPrice": 5000000,
    "bookingDate": "2024-12-04T10:30:00",
    "tourStartDate": "2024-12-15",
    "tourEndDate": "2024-12-17",
    "specialRequirements": "Vegetarian meals preferred"
  }'
```

**Expected Response:**

```
HTTP/1.1 201 Created
Location: /api/bookings/1
```

**💡 Save the booking ID from the Location header!**

```bash
export BOOKING_ID=1
```

### 3.2 Get Booking by ID

```bash
curl -X GET http://localhost:8082/api/bookings/$BOOKING_ID
```

**Expected Response:**

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
  "status": "PENDING",
  "createdAt": "2024-12-04T10:30:00",
  "specialRequirements": "Vegetarian meals preferred"
}
```

### 3.3 Get All Bookings (Paginated)

```bash
curl -X GET "http://localhost:8082/api/bookings?page=1&limit=10"
```

### 3.4 Get Bookings by User

```bash
curl -X GET "http://localhost:8082/api/bookings/user/2?page=1&limit=10"
```

### 3.5 🎉 **Confirm Booking** (TRIGGERS EMAIL!)

**This is the key step that triggers the email notification with tour timeline!**

```bash
curl -X PUT http://localhost:8082/api/bookings/$BOOKING_ID/confirm \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

**Expected Response:**

```
HTTP/1.1 200 OK
```

**What Happens:**

1. ✅ Booking status changes from `PENDING` → `CONFIRMED`
2. ✅ System fetches user info from auth service
3. ✅ System fetches tour details from tour service
4. ✅ System fetches tour timeline from tour service
5. ✅ System generates Google Calendar link
6. ✅ System sends beautiful HTML email to user with:
   - Tour details
   - Complete day-by-day timeline
   - "Add to Google Calendar" button
7. ✅ User receives email notification

**Check Logs:**

```bash
docker logs bt-service-booking -f
```

Look for:

```
INFO - Sending booking confirmation email for bookingId: 1
INFO - Fetching user info from: http://service-auth:6060/api/users/2
INFO - Fetching tour info from: http://service-tour:8081/api/tours/1
INFO - Fetching tour timeline from: http://service-tour:8081/api/tours/1/timeline
INFO - Booking confirmation email sent successfully for bookingId: 1
```

### 3.6 Reject Booking

```bash
curl -X PUT "http://localhost:8082/api/bookings/$BOOKING_ID/reject?reason=Tour+fully+booked" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

### 3.7 Cancel Booking

```bash
curl -X PUT http://localhost:8082/api/bookings/$BOOKING_ID/cancel \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

---

## 4️⃣ **COMPLETE END-TO-END FLOW**

### Full Test Script

```bash
#!/bin/bash

echo "========================================="
echo "🚀 Booking-Tour Complete Flow Test"
echo "========================================="

# Step 1: Login as User
echo "\n1️⃣ Logging in as user..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "User@123"
  }')

USER_TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.accessToken')
USER_ID=$(echo $LOGIN_RESPONSE | jq -r '.user.id')
echo "✅ Logged in. User ID: $USER_ID"

# Step 2: Browse Tours
echo "\n2️⃣ Browsing available tours..."
curl -s "http://localhost:8081/api/tours?page=1&limit=5" | jq '.[0] | {id, name, price, duration}'
echo "✅ Tours loaded"

# Step 3: Get Tour Details
echo "\n3️⃣ Getting tour details for Tour ID 1..."
curl -s http://localhost:8081/api/tours/1 | jq '{id, name, price, location, startDate, endDate}'
echo "✅ Tour details retrieved"

# Step 4: Get Tour Timeline
echo "\n4️⃣ Getting tour timeline..."
curl -s http://localhost:8081/api/tours/1/timeline | jq 'length'
echo "✅ Timeline activities found"

# Step 5: Create Booking
echo "\n5️⃣ Creating booking..."
BOOKING_LOCATION=$(curl -s -i -X POST http://localhost:8082/api/bookings \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": $USER_ID,
    \"tourId\": 1,
    \"numberOfPeople\": 2,
    \"totalPrice\": 5000000,
    \"bookingDate\": \"$(date -u +"%Y-%m-%dT%H:%M:%S")\",
    \"tourStartDate\": \"2024-12-15\",
    \"tourEndDate\": \"2024-12-17\",
    \"specialRequirements\": \"Test booking\"
  }" | grep -i location | awk '{print $2}' | tr -d '\r')

BOOKING_ID=$(echo $BOOKING_LOCATION | grep -oE '[0-9]+$')
echo "✅ Booking created. ID: $BOOKING_ID"

# Step 6: Check Booking Status
echo "\n6️⃣ Checking booking status..."
curl -s http://localhost:8082/api/bookings/$BOOKING_ID | jq '{id, status, tourId, numberOfPeople, totalPrice}'
echo "✅ Booking status: PENDING"

# Step 7: Login as Admin
echo "\n7️⃣ Logging in as admin..."
ADMIN_RESPONSE=$(curl -s -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "Admin@123"
  }')

ADMIN_TOKEN=$(echo $ADMIN_RESPONSE | jq -r '.accessToken')
echo "✅ Admin logged in"

# Step 8: Confirm Booking (TRIGGERS EMAIL!)
echo "\n8️⃣ 🎉 Confirming booking (This will trigger email!)..."
curl -s -X PUT http://localhost:8082/api/bookings/$BOOKING_ID/confirm \
  -H "Authorization: Bearer $ADMIN_TOKEN"
echo "✅ Booking confirmed!"

# Step 9: Verify Booking Status
echo "\n9️⃣ Verifying booking confirmation..."
sleep 2
curl -s http://localhost:8082/api/bookings/$BOOKING_ID | jq '{id, status, tourId, numberOfPeople}'
echo "✅ Booking status: CONFIRMED"

# Step 10: Check Logs
echo "\n🔟 Check service logs for email confirmation:"
echo "   docker logs bt-service-booking --tail 20"

echo "\n========================================="
echo "✅ Complete flow test finished!"
echo "📧 Check your email inbox for confirmation!"
echo "========================================="
```

Save as `test-flow.sh`, make executable, and run:

```bash
chmod +x test-flow.sh
./test-flow.sh
```

---

## 5️⃣ **VERIFICATION COMMANDS**

### Check Email Sent Successfully

```bash
# View booking service logs
docker logs bt-service-booking --tail 50

# Filter for email-related logs
docker logs bt-service-booking 2>&1 | grep -i "email"

# Real-time log monitoring
docker logs bt-service-booking -f
```

### Verify Database State

```bash
# Connect to MySQL
docker exec -it bt-mysql mysql -uroot -proot

# Check bookings
USE BT_BOOKING;
SELECT id, user_id, tour_id, status, created_at, updated_at FROM bookings;

# Check tour timelines
USE BT_TOUR;
SELECT * FROM tour_timelines WHERE tour_id = 1;

# Check users
USE BT_AUTH;
SELECT id, email, full_name, is_active FROM users;

# Exit MySQL
exit
```

### Test Individual Services

```bash
# Test service-auth health
curl http://localhost:6060/actuator/health

# Test service-tour health
curl http://localhost:8081/actuator/health

# Test service-booking health
curl http://localhost:8082/actuator/health
```

---

## 6️⃣ **TROUBLESHOOTING COMMANDS**

### Service Not Responding

```bash
# Check if containers are running
docker ps

# Restart specific service
docker-compose restart service-booking

# View service logs
docker logs bt-service-booking -f
docker logs bt-service-tour -f
docker logs bt-service-auth -f

# Rebuild and restart
docker-compose up -d --build service-booking
```

### Email Not Sent

```bash
# Check email configuration
docker exec bt-service-booking env | grep MAIL

# View email-related logs
docker logs bt-service-booking 2>&1 | grep -E "(email|SMTP|mail)"

# Test SMTP connection
docker exec bt-service-booking curl -v telnet://smtp.gmail.com:587
```

### Database Issues

```bash
# Check if MySQL is running
docker ps | grep mysql

# View MySQL logs
docker logs bt-mysql --tail 50

# Check database exists
docker exec -it bt-mysql mysql -uroot -proot -e "SHOW DATABASES;"

# Check if migrations ran
docker exec -it bt-mysql mysql -uroot -proot -e "USE BT_TOUR; SHOW TABLES;"
```

---

## 7️⃣ **QUICK REFERENCE**

### Default Credentials

| User Type    | Email             | Password  |
| ------------ | ----------------- | --------- |
| Admin        | admin@example.com | Admin@123 |
| Regular User | user@example.com  | User@123  |

### Service Ports

| Service         | Port | URL                   |
| --------------- | ---- | --------------------- |
| service-auth    | 6060 | http://localhost:6060 |
| service-tour    | 8081 | http://localhost:8081 |
| service-booking | 8082 | http://localhost:8082 |
| MySQL           | 3307 | localhost:3307        |

### Key Endpoints

| Action          | Method | Endpoint                   |
| --------------- | ------ | -------------------------- |
| Register        | POST   | /api/auth/register         |
| Login           | POST   | /api/auth/login            |
| Get Tours       | GET    | /api/tours                 |
| Get Timeline    | GET    | /api/tours/{id}/timeline   |
| Create Booking  | POST   | /api/bookings              |
| Confirm Booking | PUT    | /api/bookings/{id}/confirm |

---

## 📝 Notes

1. **Authentication**: Most endpoints require a Bearer token. Get it by logging in first.
2. **Email Feature**: Only triggers when booking status changes to `CONFIRMED`.
3. **Timeline**: Available for tours with ID 1 and 2 (seeded data).
4. **Pagination**: Most list endpoints support `?page=1&limit=10` parameters.
5. **Date Format**: Use ISO 8601 format (YYYY-MM-DDTHH:mm:ss) for timestamps.

---

## 🎯 Testing Checklist

- [ ] User can register successfully
- [ ] User can login and receive tokens
- [ ] User can browse tours
- [ ] User can view tour details
- [ ] User can view tour timeline
- [ ] User can create a booking
- [ ] Admin can confirm booking
- [ ] Email is sent on confirmation
- [ ] Email contains tour timeline
- [ ] Google Calendar link works
- [ ] Booking status changes to CONFIRMED

---

**Last Updated**: December 4, 2024  
**Version**: 1.0.0
