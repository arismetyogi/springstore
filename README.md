# 🛒 SpringStore - Spring Boot E-Commerce API

This project is a backend RESTful API built with **Spring Boot** for managing user authentication, product listings, cart operations, and orders. It is designed for use in an eCommerce or inventory management system.

---

## 🚀 Project Requirements

- **Java 17+**
- **Maven 3.8+**
- **Spring Boot 3.5+**
- **PostgreSQL** or compatible RDBMS
- **Postman** (for API testing)
- **Docker (optional)** – for DB and deployment
- **Lombok Plugin** enabled in your IDE
- **Open API WebMVC** with swagger-ui

---

## 🧰 Stack Used

| Layer             | Technology                         |
|-------------------|------------------------------------|
| Language          | Java 24                            |
| Framework         | Spring Boot 3.5.3                  |
| Auth              | Spring Security + JWT              |
| Data Access       | Spring Data JPA                    |
| DB                | PostgreSQL (configurable)          |
| Build Tool        | Maven                              |
| Validation        | Jakarta Bean Validation            |
| Uploads           | Multipart/Form-Data (image upload) |
| Testing           | JUnit 5, Mockito                   |
| API Documentation | Swagger-UI                         |

---

## 🔗 Entity Relationships

### 1. **User**
- `id`, `name`, `email`, `password`, `role`.
- Implements `UserDetails` for Spring Security

### 2. **Product**
- `id`, `name`, `description`, `price`, `stock`, `image`.

### 3. **Cart**
- Each user has one cart
- Cart has multiple cart items

### 4. **Order**
- Linked to a user
- Contains items, address, phone number, status (e.g. `PREPARING`, `DELIVERING`, `DELIVERED`, `CANCELED`)

---

## API Documentation
Swagger UI can be accessed on <br>
`{{base_uri}}/swagger-ui/index.html`

## Commands

- `mvn -B clean package` : for building the app and test, or add `-D skiptest` to skip tests when building the app
- `mvn test` : to run all testings

## 🛠️ API Endpoints

### 🧑‍💼 Auth

| Method | Endpoint             | Description        |
|--------|----------------------|--------------------|
| POST   | `/api/auth/login`    | Login, returns JWT |
| POST   | `/api/auth/register` | Register a new user|
| POST   | `/api/auth/change-password` | Change password (authenticated) |

---

### 📦 Products

| Method | Endpoint                   | Description                  |
|--------|----------------------------|------------------------------|
| GET    | `/api/products`            | List all products            |
| GET    | `/api/products/{id}`       | Get product by ID            |
| POST   | `/api/products`            | Create product (admin only)  |
| PUT    | `/api/products/{id}`       | Update product (admin only)  |
| DELETE | `/api/products/{id}`       | Delete product (admin only)  |

> ✅ Requires `ROLE_ADMIN` for POST/PUT/DELETE

---

### 🛒 Cart

| Method | Endpoint          | Description                  |
|--------|-------------------|------------------------------|
| GET    | `/api/cart`       | View current user's cart     |
| POST   | `/api/cart/add`   | Add item to cart             |
| DELETE | `/api/cart`       | Clear entire cart            |

> ✅ Requires authentication

---

### 📬 Orders

| Method | Endpoint                        | Description                      |
|--------|----------------------------------|----------------------------------|
| GET    | `/api/orders`                   | Get all orders (admin only)      |
| GET    | `/api/orders/user`             | Get current user's orders        |
| POST   | `/api/orders`                  | Create order from cart           |
| PUT    | `/api/orders/{orderId}/status` | Update order status (admin only) |

> ✅ Requires authentication

---

## ⚙️ Database Configuration

In `application.properties`:

```properties
# Server Config
spring.application.name=springstore
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=2MB

server.port=8081

# Database Config
spring.datasource.url=jdbc:postgresql://localhost:5432/springstore_db
spring.datasource.username=yourDBusername
spring.datasource.password=yourDBpassword

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Config
jwt.secret=very-secret-key
jwt.expiration=86400000

# File upload (optional)
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB

# Enable swagger ui
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
