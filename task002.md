---

### `task002.md` — Phase 2: Roles, Password, Profile (A5–A7, R1–R2, U1–U2)

```markdown
# Phase 2 — Roles, Password, Profile

## Scope

- A5 Change Password
- A6 Forgot / Reset Password (email link, one-time token)
- A7 Logout / Revoke Token (current/all sessions)
- R1 Assign Role (ADMIN only)
- R2 List Roles
- U1 List Users (ADMIN)
- U2 Update Profile (me)

## Endpoints

- POST `/api/auth/change-password` → body `{oldPassword,newPassword}`
- POST `/api/auth/forgot-password` → body `{email}` → send reset link
- POST `/api/auth/reset-password` → body `{resetToken,newPassword}`
- POST `/api/auth/logout` → revoke current refresh token (header or body)
- POST `/api/auth/logout-all` → revoke all refresh tokens of user
- POST `/api/auth/assign-role` (ADMIN) → `{userId, role}`
- GET `/api/auth/roles` → list roles
- GET `/api/auth/users` (ADMIN) → paging list
- PUT `/api/auth/me` → update `{fullName, avatarUrl?}`

## Data Model

- Add table `password_reset_tokens(id,user_id,token,expires_at,used)`
- Ensure indexes for `refresh_tokens(token)`, `users(email)`

## Tasks

- [ ] DTOs + validation cho change/forgot/reset password
- [ ] Service: generate reset token (UUID), TTL 30m; mark `used=true` sau khi reset
- [ ] Integrate service-notification (tạm mock log) để “gửi email” reset
- [ ] Revoke logic: current token từ header/bearer or cookie; revoke-all theo userId
- [ ] Role APIs: assign role (check ADMIN), list roles
- [ ] Users API (ADMIN): paging (page,size,sort)
- [ ] Update profile: chỉ cho user hiện tại (from JWT)
- [ ] Add @PreAuthorize guard các endpoint theo role

## Acceptance Criteria

- [ ] Change password yêu cầu đúng oldPassword; login lại OK
- [ ] Forgot → tạo reset token; Reset → mật khẩu đổi, token không dùng lại
- [ ] Logout thu hồi refresh token hiện tại; Logout-all thu hồi toàn bộ
- [ ] Assign-role chỉ ADMIN; user có role mới khi login lại
- [ ] List users trả dữ liệu paging ổn định
- [ ] Update profile cập nhật được fullName (và fields hợp lệ)
- [ ] Tài liệu Swagger cập nhật đủ endpoint

## Hints

- Sử dụng `@Transactional` cho reset-password và revoke tokens
- Rate-limit nhẹ cho forgot-password (future)
- Ghi audit basic: time, ip (header X-Forwarded-For) cho change/reset
```
