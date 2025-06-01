# Backend - Course Registration System

This is the backend service for the Course Registration System. It is built with Java and Spring Boot, providing RESTful APIs for course, enrollment, user authentication, and other registration-related features.

## How It Works
- **Framework:** Java 21+, Spring Boot
- **Database:** Connects to a relational database (see `db_init.sql` for schema)
- **Authentication:** JWT-based authentication using HS256.
- **API:** Exposes endpoints for courses, course sections, enrollments, prerequisites, terms, and user management
- **Configuration:** Main configs in `src/main/resources/application.properties`

## API
- **API Documentation:**
  - API Documentation with SwaggerUI can be found locally at the endpoint [/api-docs-ui](http://localhost:8484/api-docs-ui) 
- **API Endpoints:**
  - All endpoints are prefixed with `/api/` (e.g., `/api/courses`, `/api/enrollments`)
  - See the API Documentation for available endpoints and payload request/responses.
- **CORS:** CORS is enabled for local frontend development. CLIENT_URL must be specified as an environmental variable.
- **Error Handling:** Errors are returned as JSON with appropriate HTTP status codes.

## Useful Files
- `src/main/resources/application.properties`: Main configuration
- `db_init.sql`: Database schema and seed data
- Only files located in `dto` packages will be used for data transfer.

## Sample Accounts
- In development environment, 3 sample accounts are created.
```angular2html
Admin Account
Username: instructor@email.com
Password: 123456
```
```angular2html
Instructor Account
Username: instructor@email.com
Password: 123456
```
```angular2html
Student Account
Username: student@email.com
Password: 123456
```

## API Docs UI - Authentication
Most endpoints require authentication, and some require specific roles. Here's how to signup or login for testing purposes.

#### Create a new account
  - Use the `/api/auth/signup` endpoint to create a new account.
  - Upon success, a JWT token as a cookie will be placed for authentication.

#### Login to an existing account
  - To Log into an account using the API Docs UI, click on the green `Authorize` button at the top right of the page.
  - Enter account details and select Authorize.
  - To then authenticate with the server, use the `/api/auth/login` endpoint. This will place a JWT token as a cookie for authentication upon success.
  - To logout, use the `/api/auth/logout` endpoint. This will remove the JWT token cookie.