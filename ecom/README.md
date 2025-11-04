# Spring Boot E-commerce Project

A full-featured E-commerce backend application built with Spring Boot, providing a robust REST API for managing products, orders, carts, and user authentication.

## Features

- User Authentication and Authorization with JWT
- Product Management
- Category Management
- Shopping Cart Functionality
- Order Processing
- Address Management
- Payment Integration
- Role-based Access Control

## Technologies Used

- Java 17
- Spring Boot 3.1.x
- Spring Security
- Spring Data JPA
- Maven
- JWT (JSON Web Tokens)

## Getting Started

### Prerequisites

- JDK 17 or later
- Maven 3.6.x or later
- Your favorite IDE (VS Code, IntelliJ IDEA, Eclipse)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/riya-jindal28/Ecom-Project.git
```

2. Navigate to the project directory:
```bash
cd Ecom-Project/ecom
```

3. Build the project:
```bash
./mvnw clean install
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication
- POST `/api/auth/signup` - Register a new user
- POST `/api/auth/login` - Login user

### Products
- GET `/api/products` - Get all products
- GET `/api/products/{id}` - Get product by ID
- POST `/api/products` - Create new product (Admin only)
- PUT `/api/products/{id}` - Update product (Admin only)
- DELETE `/api/products/{id}` - Delete product (Admin only)

### Categories
- GET `/api/categories` - Get all categories
- POST `/api/categories` - Create new category (Admin only)
- PUT `/api/categories/{id}` - Update category (Admin only)

### Cart
- GET `/api/cart` - Get user's cart
- POST `/api/cart/add` - Add item to cart
- PUT `/api/cart/update` - Update cart item
- DELETE `/api/cart/remove/{itemId}` - Remove item from cart

### Orders
- GET `/api/orders` - Get user's orders
- POST `/api/orders` - Create new order
- GET `/api/orders/{id}` - Get order details

### Address
- GET `/api/address` - Get user's addresses
- POST `/api/address` - Add new address
- PUT `/api/address/{id}` - Update address
- DELETE `/api/address/{id}` - Delete address

## Security

The application uses JWT (JSON Web Tokens) for authentication. Protected endpoints require a valid JWT token in the Authorization header:

```
Authorization: Bearer <token>
```

## Configuration

Application properties can be configured in `src/main/resources/application.properties`

## Docker Support

The project includes a Dockerfile for containerization. To build and run the Docker image:

```bash
# Build Docker image
docker build -t ecom-project .

# Run Docker container
docker run -p 8080:8080 ecom-project
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request