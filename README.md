# ⚡ Headless Auth & OTP API

A plug-and-play Spring Boot microservice designed to handle secure JWT generation, 2FA (Two-Factor Authentication), and password reset OTPs. 

Built as the foundational authentication brick for a modern, Composable Commerce (MACH) platform.

## 🛑 The Problem
Authentication in e-commerce monoliths gets messy fast. Controllers get bloated, circular dependencies form between `UserService` and `SecurityConfig`, and session management becomes a nightmare to scale.

## 💡 The Solution
This API extracts authentication into a completely isolated, stateless microservice. 
* Hand it an email -> It sends a secure OTP.
* Hand it an OTP -> It returns a validated JWT.
* **Zero coupling to your core product database.**

## 🛠️ Tech Stack
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security & JWT (JSON Web Tokens)
* **Architecture:** Domain-Driven Design (Package by Feature)
* **Language:** Java 17+

## 🚀 Endpoints (Coming Soon)
* `POST /api/v1/auth/otp/generate` - Generates and emails a 6-digit OTP.
* `POST /api/v1/auth/otp/validate` - Validates the OTP and issues a JWT.
* `POST /api/v1/auth/token/refresh` - Issues a new JWT from a refresh token.

---
*Committing daily until V1 is ready for production.*
