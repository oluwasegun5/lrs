# Learning Record Store (LRS) API

A Spring Boot application for managing learning records and xAPI statements using MongoDB.

## Features

- ✅ xAPI Statement management
- ✅ Learning Record tracking
- ✅ MongoDB integration
- ✅ RESTful API endpoints
- ✅ Lombok for cleaner code
- ✅ Comprehensive logging
- ✅ **Swagger/OpenAPI Documentation** - Interactive API documentation

## Technologies

- Java 17
- Spring Boot 3.5.6
- MongoDB
- Lombok
- Maven
- Swagger/OpenAPI 3.0

## Prerequisites

- Java 17+
- Maven 3.6+
- MongoDB (running on localhost:27017)

## Installation

1. Clone the repository
2. Make sure MongoDB is running
3. Build the project (this will download all dependencies including Swagger):
```bash
./mvnw clean install
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8088`

## 📚 API Documentation (Swagger)

Once the application is running, you can access the interactive API documentation:

- **Swagger UI**: http://localhost:8088/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8088/api-docs

The Swagger UI provides:
- Complete API documentation
- Interactive testing of all endpoints
- Request/response schemas
- Example values
- Try-it-out functionality

## API Endpoints

