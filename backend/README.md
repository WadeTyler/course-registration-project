# Backend - Course Registration System

This is the backend service for the Course Registration System. It is built with Java and Spring Boot, providing RESTful APIs for course, enrollment, user authentication, and other registration-related features.

## How It Works
- **Framework:** Java 17+, Spring Boot
- **Database:** Connects to a relational database (see `db_init.sql` for schema)
- **Authentication:** JWT-based authentication (see `src/main/resources/certs/` for keys)
- **API:** Exposes endpoints for courses, course sections, enrollments, prerequisites, terms, and user management
- **Configuration:** Main configs in `src/main/resources/application.properties`

## For Frontend Developers
- **API Endpoints:**
  - All endpoints are prefixed with `/api/` (e.g., `/api/courses`, `/api/enrollments`)
  - Most endpoints require a valid JWT token in the `Authorization` header: `Bearer <token>`
  - See the backend code for available endpoints and request/response formats
- **CORS:** CORS is enabled for local frontend development
- **Error Handling:** Errors are returned as JSON with appropriate HTTP status codes
- **Running Locally:**
  - Use `mvnw spring-boot:run` in the `backend/` directory
  - Configure environment variables or edit `application.properties` as needed
- **Database:**
  - The backend expects a running database instance; see `db_init.sql` for setup
  - Default DB connection settings can be found in `application.properties`

## Useful Files
- `src/main/resources/application.properties`: Main configuration
- `src/main/resources/certs/`: JWT signing keys
- `db_init.sql`: Database schema and seed data

## Contact
For questions about backend API integration, contact the backend team or check the code for endpoint details.

