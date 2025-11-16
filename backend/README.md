# IQBooster Backend API

Simple REST API backend for the IQBooster brain game Android application.

## Features

- User registration with password hashing (bcrypt)
- User login with session token generation
- SQLite database for data persistence
- CORS enabled for cross-origin requests

## Tech Stack

- **Runtime**: Node.js
- **Framework**: Express.js
- **Database**: SQLite3
- **Security**: bcrypt for password hashing
- **Session**: UUID-based tokens

## Installation

```bash
cd backend
npm install
```

## Running the Server

### Development mode (with auto-reload):
```bash
npm run dev
```

### Production mode:
```bash
npm start
```

The server will start on port 3000 by default. You can change this by setting the `PORT` environment variable:

```bash
PORT=8080 npm start
```

## API Endpoints

### 1. Register User
**Endpoint**: `POST /api/register`

**Request Body**:
```json
{
  "username": "testuser",
  "password": "password123",
  "mailbox": "user@example.com",
  "sex": "male"
}
```

**Response** (200 OK):
```json
{
  "status_code": 200,
  "message": "Registration successful",
  "id": 1,
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "userId": 1
}
```

**Error Responses**:
- 400: Missing username or password
- 409: Username already exists
- 500: Server error

### 2. Login User
**Endpoint**: `POST /api/login`

**Request Body**:
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "status_code": 200,
  "message": "Login successful",
  "id": 1,
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "userId": 1
}
```

**Error Responses**:
- 400: Missing username or password
- 401: Invalid credentials
- 500: Server error

### 3. Health Check
**Endpoint**: `GET /api/health`

**Response**:
```json
{
  "status": "ok",
  "message": "IQBooster API is running"
}
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT UNIQUE NOT NULL,
  password TEXT NOT NULL,
  mailbox TEXT,
  sex TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### Sessions Table
```sql
CREATE TABLE sessions (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER NOT NULL,
  token TEXT UNIQUE NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## Android App Configuration

Update the Android app's `Constant.java` file to point to your server:

```java
public static final String BASE_URL = "http://YOUR_SERVER_IP:3000/api";
public static final String REGISTER_URL = "http://YOUR_SERVER_IP:3000/api/register";
public static final String LOGIN_URL = "http://YOUR_SERVER_IP:3000/api/login";
```

For local testing with Android emulator, use:
- `http://10.0.2.2:3000/api` (for Android emulator)
- `http://YOUR_LOCAL_IP:3000/api` (for physical device on same network)

## Security Notes

- Passwords are hashed using bcrypt with 10 salt rounds
- Session tokens are UUID v4 strings
- CORS is enabled for all origins (configure for production use)
- For production, consider adding:
  - Rate limiting
  - HTTPS/TLS
  - Token expiration
  - Input sanitization
  - Database connection pooling

## Testing with cURL

**Register**:
```bash
curl -X POST http://localhost:3000/api/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"pass123","mailbox":"test@example.com","sex":"male"}'
```

**Login**:
```bash
curl -X POST http://localhost:3000/api/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"pass123"}'
```

## Deployment

For production deployment, consider:

1. **Heroku**: Simple deployment with Git
2. **Railway**: Free tier with easy setup
3. **DigitalOcean**: VPS with full control
4. **AWS/GCP/Azure**: Cloud platforms with scaling

## License

This backend is part of the IQBooster project.
