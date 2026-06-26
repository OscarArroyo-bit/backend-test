# Backend Technical Test

## Overview

This project is a backend solution built using a microservices architecture with Spring Boot 3. It simulates an order processing system where orders are created, processed asynchronously through Apache Kafka, and their status is updated based on the payment result.

Sensitive payment information is protected using RSA encryption before being sent through Kafka.

---

## Architecture

```
Client
   |
   v
OrderMS
   |
   |  OrderPlacedEvent
   v
Apache Kafka
   |
   v
PaymentMS
   |
   |  PaymentProcessedEvent
   v
Apache Kafka
   |
   v
OrderMS
   |
   v
PostgreSQL
```

## Features

- Create Orders
- Retrieve Orders
- Asynchronous communication using Kafka
- Payment processing simulation
- RSA encryption for card information
- Order status updates
- Global exception handling
- Dockerized environment

---

## Project Structure

```
backend-test
│
├── order-ms
│
├── payment-ms
│
└── docker-compose.yml
```

---

## Running the Project

### Requirements

- Docker Desktop
- Maven
- git
- Postman

### Start the application

```bash
docker compose up --build
```

The following containers will be created:

- PostgreSQL
- Apache Kafka
- OrderMS
- PaymentMS

---

## API Endpoints

### Create Order

```
POST /orders
```

Example Request

POST http://localhost:8080/orders

```json
{
  "productName": "Laptop",
  "quantity": 1,
  "price": 1200.00,
  "cardNumber": "4111111111111111"
}
```

---

### Get Order by Id

```
GET /orders/{id}
```

Example

```
GET http://localhost:8080/orders/2
```

---

### Get All Orders

Example

```
GET http://localhost:8080/orders
```


## Security

Before an order is published to Kafka, the credit card number is encrypted using RSA.

The Payment Service decrypts the information before processing the payment.

This ensures that sensitive information never travels through Kafka in plain text.


## Validation Script

```

Follow the steps below to validate the complete asynchronous flow.

1. Start the application
docker compose up --build

Wait until all containers are running.

2. Create a new Order

POST

http://localhost:8080/orders

Request Body

{
  "productName": "Laptop",
  "quantity": 1,
  "price": 1200.00,
  "cardNumber": "4111111111111111"
}

The response will return the created order.

3. Check the Initial Order Status

Replace {id} with the generated order id.

GET

http://localhost:8080/orders/{id}

Example

GET http://localhost:8080/orders/1

The initial status should be:

PENDING

4. Verify Kafka Communication

Observe the logs of both microservices.

OrderMS

You should see messages similar to:

Publishing order 1 to Kafka

PaymentMS

You should see messages similar to:

ORDER RECEIVED

Payment approved

or

ORDER RECEIVED

Payment rejected

5. Verify Final Order Status

Execute again:

GET http://localhost:8080/orders/{id}

The status should now be either:

PAID

or

PAYMENT_FAILED

depending on the simulated payment result.

```

Examples include:

- Order not found
- Validation errors
- Internal server errors

## Author

Oscar Arroyo Sanchez