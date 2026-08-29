# Product Stock API

REST API for inventory control, with category and product management, logical deletion, and centralized error handling. Personal project.

---

## Tech Stack

| Technology | Details |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.0 |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL 8.4 |
| Migrations | Flyway |
| Validation | Bean Validation |
| Build Tool | Maven (wrapper `./mvnw` included) |

---

## Data Model

### Entities

| Entity | Fields |
|---|---|
| Category | `id`, `name`, `active`, `productCount` |
| Product | `id`, `name`, `price`, `stock`, `active`, `category` |

### Relationships

| Relationship | Type |
|---|---|
| Category → Product | One-to-Many |

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
| `409` | Business rule violation (e.g. deleting a category with linked products) |
| `500` | Catch-all for unexpected errors |

---

## Design Decisions

- Business rules (logical deletion, state validation) are encapsulated in the entities, not in the controllers.
- `productCount` is kept as a persisted field on `Category`, updated on every product creation, deletion, or category change — a conscious trade-off instead of computing it via `COUNT` at read time.
- Logical deletion (`active = false`) is used instead of physical removal.

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

The application reads database credentials from environment variables — no credentials are hardcoded.

| Variable | Description |
|---|---|
| `DB_USERNAME` | MySQL username |
| `DB_PASSWORD` | MySQL password |

**Via terminal:**
```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

**Via IntelliJ:** Run > Edit Configurations > Environment Variables, and add both variables there.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

On the first run, Flyway automatically applies the migrations, creating the `categories` and `products` tables with the full structure already in place — no manual script needed.

### 5. Test it out

The API starts on `http://localhost:8080`. Use any HTTP client (Insomnia, Postman, curl) to test the endpoints listed above. Quick example with curl:

```bash
curl -X POST http://localhost:8080/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Electronics"}'
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
- [ ] Spring Security + JWT authentication
- [ ] Docker + Docker Compose setup
- [ ] Integration tests
- [ ] Filter endpoints (e.g. `GET /products?category=`)
