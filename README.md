# AgriChain API

A RESTful backend API for a Nigerian agri-fintech supply chain platform that connects smallholder farmers to off-takers (buyers) through managed collection centers.

Built with Spring Boot 4, secured with JWT authentication, and documented with Swagger UI.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.7 |
| Security | Spring Security 7 + JWT |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven |

---

## Features

- Role-based access control with three roles: **FARMER**, **OFF_TAKER**, **ADMIN**
- JWT authentication with access tokens (1 hour) and refresh tokens (7 days)
- Full supply chain flow: produce catalogue → farmer inventory → orders → payments
- Global exception handling with consistent JSON error responses
- Interactive API documentation at `/docs`

---

## API Overview

| Resource | Base Path | Description |
|---|---|---|
| Authentication | `/api/v1/auth` | Register, login, logout |
| Users | `/api/v1/users` | User management and profile |
| Farmers | `/api/v1/farmers` | Registered farmer profiles |
| Off-takers | `/api/v1/offtakers` | Registered buyer profiles |
| Produce | `/api/v1/produce` | Produce catalogue (types, pricing) |
| Farmer Produce | `/api/v1/farmer-produce` | Farmer inventory entries |
| Collection Centers | `/api/v1/collection-centers` | Physical delivery hubs |
| Orders | `/api/v1/orders` | Order placement and lifecycle |
| Payments | `/api/v1/payments` | Payment tracking |

Full request/response schemas and access rules are documented in Swagger UI.

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL database

### Local Setup

**1. Clone the repository**

```bash
git clone https://github.com/ikechukwudennis/agrichain-api.git
cd agrichain-api
```

**2. Configure environment variables**

Copy the example file and fill in your values:

```bash
cp .env.example .env
```

Then set the variables in your IDE run configuration or export them in your shell. See `.env.example` for the full list of required variables.

**3. Run the application**

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`. Visiting the root URL redirects to the Swagger UI at `/docs`.

---

## Environment Variables

| Variable | Description |
|---|---|
| `DATABASE_URL` | Full JDBC connection string (e.g. `jdbc:postgresql://host/db`) |
| `DATABASE_USERNAME` | Database user |
| `DATABASE_PASSWORD` | Database password |
| `JWT_SECRET_KEY` | Base64-encoded secret key (min 32 characters) |
| `DDL_AUTO` | Hibernate DDL mode — use `update` locally, `validate` in production |

See `.env.example` for the expected format.

---

## Authentication

All protected endpoints require a Bearer token in the `Authorization` header:

```
Authorization: Bearer <access_token>
```

Tokens are returned on register and login. Access tokens expire after **1 hour**. Use the refresh token to get a new one without re-logging in.

---

## Deployment

This project is containerised with Docker and deployed on [Render](https://render.com) with a [Neon](https://neon.tech) PostgreSQL database.

To build the Docker image locally:

```bash
docker build -t agrichain-api .
docker run -p 8080:8080 --env-file .env agrichain-api
```

---

## License

This project is open source and available under the [MIT License](LICENSE).
