# Quick Test Commands - Copy & Paste Ready

This is a condensed version with ready-to-use commands for quick testing.

## 🚀 Quick Start (5 Minutes)

### Prerequisites

```bash
# Start services
docker-compose up -d

# Wait for services to be ready (30 seconds)
sleep 30
```

---

## ⚡ RAPID TEST FLOW

### 1. Login as User

```bash
curl -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "User@123"}' \
  | jq -r '.accessToken'
```

**Copy the token and set it:**

```bash
export TOKEN="paste_token_here"
```

### 2. Browse Tours

```bash
curl http://localhost:8081/api/tours | jq
```

### 3. View Tour Timeline (NEW!)

```bash
curl http://localhost:8081/api/tours/1/timeline | jq
```

### 4. Create Booking

```bash
curl -X POST http://localhost:8082/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "tourId": 1,
    "numberOfPeople": 2,
    "totalPrice": 5000000,
    "bookingDate": "2024-12-04T10:00:00",
    "tourStartDate": "2024-12-15",
    "tourEndDate": "2024-12-17",
    "specialRequirements": "Test booking"
  }' -i | grep Location
```

**Save booking ID:**

```bash
export BOOKING_ID=1
```

### 5. Login as Admin

```bash
curl -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@example.com", "password": "Admin@123"}' \
  | jq -r '.accessToken'
```

**Set admin token:**

```bash
export ADMIN_TOKEN="paste_admin_token_here"
```

### 6. Confirm Booking (SENDS EMAIL!)

```bash
curl -X PUT http://localhost:8082/api/bookings/$BOOKING_ID/confirm \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

### 7. Check Logs

```bash
docker logs bt-service-booking --tail 20
```

### 8. Verify Booking

```bash
curl http://localhost:8082/api/bookings/$BOOKING_ID | jq
```

---

## 🎯 ONE-LINER COMPLETE TEST

```bash
# Complete flow in one command chain
USER_TOKEN=$(curl -s -X POST http://localhost:6060/api/auth/login -H "Content-Type: application/json" -d '{"email":"user@example.com","password":"User@123"}' | jq -r '.accessToken') && \
BOOKING_LOC=$(curl -s -i -X POST http://localhost:8082/api/bookings -H "Content-Type: application/json" -d '{"userId":2,"tourId":1,"numberOfPeople":2,"totalPrice":5000000,"bookingDate":"2024-12-04T10:00:00","tourStartDate":"2024-12-15","tourEndDate":"2024-12-17"}' | grep -i location | awk '{print $2}' | tr -d '\r') && \
BOOKING_ID=$(echo $BOOKING_LOC | grep -oE '[0-9]+$') && \
ADMIN_TOKEN=$(curl -s -X POST http://localhost:6060/api/auth/login -H "Content-Type: application/json" -d '{"email":"admin@example.com","password":"Admin@123"}' | jq -r '.accessToken') && \
curl -X PUT http://localhost:8082/api/bookings/$BOOKING_ID/confirm -H "Authorization: Bearer $ADMIN_TOKEN" && \
echo "\n✅ Booking $BOOKING_ID confirmed! Check email and logs."
```

---

## 📋 INDIVIDUAL API TESTS

### Authentication

```bash
# Register
curl -X POST http://localhost:6060/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test@123","fullName":"Test User"}'

# Login
curl -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"User@123"}'

# Get Profile
curl http://localhost:6060/api/auth/me \
  -H "Authorization: Bearer $TOKEN"
```

### Tours

```bash
# List all tours
curl http://localhost:8081/api/tours

# Get tour by ID
curl http://localhost:8081/api/tours/1

# Search tours
curl "http://localhost:8081/api/tours/search?q=Phú Quốc"

# Get tour timeline (NEW!)
curl http://localhost:8081/api/tours/1/timeline

# Get tours by category
curl http://localhost:8081/api/tours/category/1
```

### Bookings

```bash
# Create booking
curl -X POST http://localhost:8082/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"userId":2,"tourId":1,"numberOfPeople":2,"totalPrice":5000000,"bookingDate":"2024-12-04T10:00:00","tourStartDate":"2024-12-15","tourEndDate":"2024-12-17"}'

# Get booking
curl http://localhost:8082/api/bookings/1

# List all bookings
curl http://localhost:8082/api/bookings

# Get user bookings
curl http://localhost:8082/api/bookings/user/2

# Confirm booking (ADMIN)
curl -X PUT http://localhost:8082/api/bookings/1/confirm \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# Cancel booking
curl -X PUT http://localhost:8082/api/bookings/1/cancel

# Reject booking (ADMIN)
curl -X PUT "http://localhost:8082/api/bookings/1/reject?reason=Fully+booked" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

---

## 🔍 VERIFICATION COMMANDS

### Check Services Running

```bash
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
```

### Check Logs for Email

```bash
# Real-time
docker logs bt-service-booking -f

# Last 50 lines
docker logs bt-service-booking --tail 50

# Filter email logs
docker logs bt-service-booking 2>&1 | grep -i "email\|confirmation"
```

### Check Database

```bash
# Bookings
docker exec -it bt-mysql mysql -uroot -proot -e "USE BT_BOOKING; SELECT id, user_id, tour_id, status FROM bookings;"

# Timeline data
docker exec -it bt-mysql mysql -uroot -proot -e "USE BT_TOUR; SELECT COUNT(*) as timeline_count FROM tour_timelines;"

# Users
docker exec -it bt-mysql mysql -uroot -proot -e "USE BT_AUTH; SELECT id, email, full_name FROM users;"
```

---

## 🐛 TROUBLESHOOTING

### Service Not Responding

```bash
# Check status
docker ps

# Restart service
docker-compose restart service-booking

# View logs
docker logs bt-service-booking --tail 50

# Rebuild
docker-compose up -d --build
```

### Email Not Sent

```bash
# Check config
docker exec bt-service-booking env | grep MAIL

# Check SMTP logs
docker logs bt-service-booking 2>&1 | grep -E "(SMTP|mail|email)"
```

### Can't Connect to Services

```bash
# Check network
docker network ls
docker network inspect booking-tour_default

# Check service URLs
curl http://localhost:6060/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
```

---

## 💾 SAVED VARIABLES (For Session)

```bash
# Export these after login
export USER_TOKEN="your_user_token_here"
export ADMIN_TOKEN="your_admin_token_here"
export BOOKING_ID=1
export TOUR_ID=1
export USER_ID=2

# Use in commands
curl http://localhost:6060/api/auth/me -H "Authorization: Bearer $USER_TOKEN"
curl http://localhost:8082/api/bookings/$BOOKING_ID
```

---

## 📊 TEST DATA

### Seeded Users

- **Admin**: admin@example.com / Admin@123
- **User**: user@example.com / User@123

### Seeded Tours

- **Tour 1**: Phú Quốc 3N2Đ (ID: 1) - Price: 2,500,000 VND
- **Tour 2**: Sa Pa 2N1Đ (ID: 2) - Price: 1,500,000 VND

### Timeline Data

- Tour 1 has 6 activities (3 days)
- Tour 2 has 5 activities (2 days)

---

## 🎬 DEMO SCRIPT

```bash
#!/bin/bash
echo "🎬 Starting Demo..."

# 1. Login
echo "\n📝 1. Login as user..."
TOKEN=$(curl -s -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"User@123"}' | jq -r '.accessToken')
echo "✅ Token: ${TOKEN:0:20}..."

# 2. Browse tours
echo "\n🏖️ 2. Browse tours..."
curl -s http://localhost:8081/api/tours | jq '.[0] | {id, name, price}'

# 3. View timeline
echo "\n📅 3. View tour timeline..."
curl -s http://localhost:8081/api/tours/1/timeline | jq -c 'length' | xargs echo "Activities:"

# 4. Create booking
echo "\n🎫 4. Create booking..."
BOOKING=$(curl -s -i -X POST http://localhost:8082/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"userId":2,"tourId":1,"numberOfPeople":2,"totalPrice":5000000,"bookingDate":"2024-12-04T10:00:00","tourStartDate":"2024-12-15","tourEndDate":"2024-12-17"}')
BOOKING_ID=$(echo "$BOOKING" | grep -i location | grep -oE '[0-9]+$')
echo "✅ Booking ID: $BOOKING_ID"

# 5. Confirm booking
echo "\n✅ 5. Confirm booking..."
ADMIN=$(curl -s -X POST http://localhost:6060/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"Admin@123"}' | jq -r '.accessToken')
curl -s -X PUT http://localhost:8082/api/bookings/$BOOKING_ID/confirm \
  -H "Authorization: Bearer $ADMIN"

echo "\n\n🎉 Demo complete!"
echo "📧 Check email for confirmation!"
echo "📋 View logs: docker logs bt-service-booking --tail 20"
```

Save as `demo.sh` and run: `chmod +x demo.sh && ./demo.sh`

---

## ✅ SUCCESS INDICATORS

After confirming a booking, you should see:

**In Logs:**

```
✅ Sending booking confirmation email for bookingId: 1
✅ Fetching user info from: http://service-auth:6060/api/users/2
✅ Fetching tour info from: http://service-tour:8081/api/tours/1
✅ Fetching tour timeline from: http://service-tour:8081/api/tours/1/timeline
✅ Booking confirmation email sent successfully for bookingId: 1
```

**In Email:**

- Subject: "🎉 Your Tour Booking is Confirmed - Tour Phú Quốc 3N2Đ"
- Contains: Tour details, timeline, calendar link
- Has: "Add to Google Calendar" button

**In Database:**

```sql
status = 'CONFIRMED'
updated_at = [current timestamp]
```

---

**Quick Start Time**: ~2 minutes  
**Full Test Time**: ~5 minutes  
**Last Updated**: December 4, 2024
