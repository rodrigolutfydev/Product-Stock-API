# Product Stock API

REST API for inventory control, with category and product management, logical deletion, JWT-based authentication, and centralized error handling. Personal project.

---

## Tech Stack

| Technology | Details |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.0 |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL 8.4 |
| Migrations | Flyway |
| Security | Spring Security + JWT (auth0/java-jwt) |
| Validation | Bean Validation |
| Build Tool | Maven (wrapper `./mvnw` included) |

---

## Data Model

### Entities

| Entity | Fields |
|---|---|
| Category | `id`, `name`, `active`, `productCount` |
| Product | `id`, `name`, `price`, `stock`, `active`, `category` |
| User | `id`, `login`, `password` |

### Relationships

| Relationship | Type |
|---|---|
| Category → Product | One-to-Many |

---

## Authentication

The API uses **JWT (JSON Web Token)** for stateless authentication.

### How it works

1. The client sends `login` and `password` to `POST /login`.
2. If the credentials are valid, the API returns a signed JWT token.
3. The client must send this token in the `Authorization` header (`Bearer <token>`) on every subsequent request to a protected endpoint.
4. A custom filter (`SecurityFilter`) validates the token on each request and authenticates the user in Spring Security's context — no server-side session is created.

### Login — `/login`

| Method | Endpoint | Auth required |
|---|---|---|
| POST | `/login` | No |

**Request:**
```json
POST /login

{
  "login": "admin",
  "password": "123456"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Using the token on protected endpoints:**
```bash
curl http://localhost:8080/categories \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

All endpoints under `/categories` and `/products` require a valid token. Requests without a token, or with an invalid/expired one, return `401 Unauthorized`.

---

## API Endpoints

### Categories — `/categories`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/categories` | Create a category |
| GET | `/categories` | List active categories (paginated) |
| GET | `/categories/{id}` | Get a category by ID |
| PUT | `/categories` | Update a category |
| DELETE | `/categories/{id}` | Logically delete a category (blocked if linked active products exist) |

### Products — `/products`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/products` | Create a product |
| GET | `/products` | List active products (paginated) |
| GET | `/products/{id}` | Get a product by ID |
| PUT | `/products` | Update a product (including category change) |
| DELETE | `/products/{id}` | Logically delete a product |

---

## Error Handling

Centralized via `@RestControllerAdvice` (`ErrorHandler`):

| Status | Meaning |
|---|---|
| `404` | Resource not found |
| `400` | Bean Validation failure or malformed request body |
| `401` | Missing/invalid credentials or invalid/expired JWT token |
| `403` | Authenticated but not allowed to access the resource |
| `409` | Business rule violation (e.g. deleting a category with linked products) |
| `500` | Catch-all for unexpected errors |

---

## Design Decisions

- Business rules (logical deletion, state validation) are encapsulated in the entities, not in the controllers.
- `productCount` is kept as a persisted field on `Category`, updated on every product creation, deletion, or category change — a conscious trade-off instead of computing it via `COUNT` at read time.
- Logical deletion (`active = false`) is used instead of physical removal.
- Authentication is stateless: no HTTP session is created, and every request is authenticated independently via the JWT token.
- `User` implements Spring Security's `UserDetails` directly, avoiding an extra adapter class.

---

## Getting Started

### Prerequisites

- Java 17+
- MySQL 8+
- Maven (or the included `./mvnw` wrapper)

### 1. Clone the repository

```bash
git clone https://github.com/rodrigolutfydev/product-stock.git
cd product-stock
```

### 2. Create the database

Connect to MySQL and create an empty database:

```sql
CREATE DATABASE product_stock;
```

### 3. Configure environment variables

The application reads database credentials and the JWT secret from environment variables — no credentials are hardcoded.

| Variable | Description |
|---|---|
| `DB_USERNAME` | MySQL username |
| `DB_PASSWORD` | MySQL password |
| `JWT_SECRET` | Secret key used to sign and verify JWT tokens |

**Via terminal:**
```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret
```

**Via IntelliJ:** Run > Edit Configurations > Environment Variables, and add the variables there.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

On the first run, Flyway automatically applies the migrations, creating the `categories`, `products`, and `users` tables with the full structure already in place — no manual script needed.

### 5. Create a user

Since there's no registration endpoint yet, insert a user directly into the database with an already-encrypted password (BCrypt):

```sql
INSERT INTO users (login, password) VALUES ('admin', '$2a$10$...');
```

### 6. Test it out

The API starts on `http://localhost:8080`. Use any HTTP client (Insomnia, Postman, curl) to test the endpoints listed above. Quick example with curl:

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login": "admin", "password": "123456"}'
```

---

## Example Requests

**Create a category:**
```json
POST /categories

{
  "name": "Electronics"
}
```

**Create a product:**
```json
POST /products

{
  "name": "Wireless Mouse",
  "price": 79.90,
  "stock": 50,
  "category": {
    "id": 1
  }
}
```

**Update a product:**
```json
PUT /products

{
  "id": 1,
  "name": "Wireless Mouse",
  "price": 89.90,
  "stock": 45,
  "category": {
    "id": 1
  }
}
```

---

## Roadmap

- [x] Full CRUD for Category and Product
- [x] Logical deletion with business-rule protection
- [x] Centralized exception handling
- [x] Flyway migrations
- [x] Pagination on list endpoints
- [x] Spring Security + JWT authentication
- [ ] User registration endpoint
- [ ] Docker + Docker Compose setup
- [ ] Integration tests
- [ ] Filter endpoints (e.g. `GET /products?category=`)
