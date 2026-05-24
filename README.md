# service-provider

Enterprise-grade Spring Boot 3 microservice for service provider management.

**Stack:** Java 22 · Spring Boot 3.3 · MongoDB · Spring Security · JWT · AES-256 response encryption · Swagger/OpenAPI · Docker

---

## Features

- JWT stateless authentication (ADMIN / CUSTOMER roles)
- CRUD for `Provider` entity with MongoDB
- AES-256-CBC response encryption on all non-auth endpoints
- Global exception handling with structured error responses
- OpenAPI 3 / Swagger UI documentation
- Multi-stage Docker build

---

## Project Structure

```
src/main/java/com/serviceprovider/
├── controller/          # REST controllers
├── service/             # Business logic
├── repository/          # Spring Data MongoDB repositories
├── entity/              # MongoDB documents
├── dto/                 # Request/response DTOs
├── mapper/              # Entity ↔ DTO mapping
├── security/            # JWT filter, token provider, UserDetailsService
├── config/              # Security, Mongo, OpenAPI, response encryption configs
├── exception/           # Custom exceptions + global handler
└── util/                # Constants
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 22+ |
| Maven | 3.9+ |
| MongoDB | 6+ |
| Docker | 24+ (optional) |

---

## Quick Start

### 1. Clone & configure

```bash
git clone https://github.com/your-org/service-provider.git
cd service-provider
```

Edit `src/main/resources/application.yml` **or** set environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `MONGODB_URI` | `mongodb://localhost:27017` | MongoDB connection string |
| `MONGODB_DATABASE` | `service_provider_db` | Database name |
| `JWT_SECRET_KEY` | (built-in dev key) | Base64 256-bit secret — **change in production** |
| `JWT_EXPIRATION` | `86400000` | Token TTL in milliseconds (24 h) |
| `ENCRYPTION_SECRET_KEY` | (built-in dev key) | AES encryption key — **change in production** |
| `SERVER_PORT` | `8080` | Server port |

### 2. Run locally

```bash
mvn spring-boot:run
```

### 3. Run with Docker

```bash
# Build image
docker build -t service-provider:latest .

# Run (with external MongoDB)
docker run -p 8080:8080 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017 \
  -e JWT_SECRET_KEY=<your-base64-secret> \
  -e ENCRYPTION_SECRET_KEY=<your-aes-key> \
  service-provider:latest
```

### 4. Run tests

```bash
mvn test
```

---

## API Documentation

Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

---

## Authentication

### Register

```
POST /auth/register
Content-Type: application/json

{
  "username": "adminuser",
  "password": "Secure@1234",
  "email": "admin@example.com",
  "role": "ADMIN"
}
```

**Response:**
```json
{
  "token": "eyJhbGci...",
  "username": "adminuser",
  "role": "ADMIN",
  "expiresIn": 86400000
}
```

### Login

```
POST /auth/login
Content-Type: application/json

{
  "username": "adminuser",
  "password": "Secure@1234"
}
```

Use the returned `token` as `Authorization: Bearer <token>` on protected endpoints.

---

## Provider Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/providers` | ADMIN | Create provider |
| `GET` | `/providers` | ADMIN | List all providers |
| `GET` | `/providers/{code}` | Public | Get provider by code |
| `PUT` | `/providers/{code}` | ADMIN | Update provider |
| `DELETE` | `/providers/{code}` | ADMIN | Delete provider |

### Sample Request – Create Provider

```
POST /providers
Authorization: Bearer <token>
Content-Type: application/json

{
  "code": "leo-tv",
  "Name": "Leo Tv",
  "Colors": {
    "Bright": "#F2C858",
    "Dark": "#201E1F",
    "Theme": "#F7C236"
  },
  "Hosts": {
    "Falcon": "7aeed.store:80",
    "HULK": "hulascw.space:8080",
    "Mac": "mac-tv.live:8880",
    "Qek": "quick4k.online",
    "Shark": "s5eed.site:80",
    "Strong": "newt4k.space",
    "Unevers": "unevapp.space:2095",
    "Vulcher": "sardk.top:80",
    "X iptv": "vipxv.com:80",
    "e4": "sadbw.top",
    "ssss": "alfaasd.space:80"
  },
  "Icon": "https://firebasestorage.googleapis.com/v0/b/oldleo.appspot.com/o/icon.png",
  "Links": {
    "Telegram": "https://t.me/leo_store11",
    "Website": "https://leos20.com/",
    "Whatsapp": "https://wa.me/966501070573"
  },
  "Offers": {
    "Whatsapp": "https://firebasestorage.googleapis.com/v0/b/masteriptv-61b16.appspot.com/o/LeoOffers.png"
  },
  "Ads": {
    "ad1": "https://firebasestorage.googleapis.com/v0/b/masteriptv-61b16.appspot.com/o/LeoOffers.png"
  }
}
```

> `code` is your URL-friendly unique identifier (e.g. `"leo-tv"`).  
> All other keys follow the PascalCase format of the original object.

### Sample Encrypted Response

All provider endpoint responses are AES-256-CBC encrypted:

```json
{
  "encryptedData": "V2VsY29tZSB0byBTZXJ2aWNlUHJvdmlkZXIhBase64..."
}
```

### Decryption (JavaScript example)

```javascript
const CryptoJS = require('crypto-js');

function decrypt(encryptedBase64, secretKey) {
  const combined = CryptoJS.enc.Base64.parse(encryptedBase64);
  const iv  = CryptoJS.lib.WordArray.create(combined.words.slice(0, 4));  // first 16 bytes
  const ct  = CryptoJS.lib.WordArray.create(combined.words.slice(4));     // remainder

  const keyBytes = CryptoJS.enc.Utf8.parse(secretKey.padEnd(32, '\0').substring(0, 32));

  const decrypted = CryptoJS.AES.decrypt(
    { ciphertext: ct },
    keyBytes,
    { iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 }
  );
  return JSON.parse(decrypted.toString(CryptoJS.enc.Utf8));
}
```

---

## Error Response Format

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Provider not found with code: PROV999",
  "path": "/providers/PROV999",
  "timestamp": "2024-01-15T10:30:00"
}
```

Validation errors include a `validationErrors` map:

```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Request validation failed",
  "validationErrors": {
    "ip": "Invalid IP address format",
    "code": "Code is required"
  }
}
```

---

## Security Notes

- **Change default secrets before deploying to production.**
- JWT secret must be a Base64-encoded string of at least 256 bits (32 bytes).
- AES key is padded/truncated to 32 bytes at runtime (AES-256).
- A fresh random IV is generated per response; the IV is prepended to the ciphertext before Base64 encoding.
- Passwords are hashed with BCrypt (strength 10).
