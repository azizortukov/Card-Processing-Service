# Card Processing Service

## Overview

This project is a backend service for card processing, built using Java 17 and Spring Boot 3. The service allows users
to create cards, withdraw and top up funds, block and unblock cards, and retrieve transaction history. The service also
utilizes Redis for caching data and is containerized using Docker.

## Features

- **Create New Card**: Allows users to create a new card with specific details and status.
- **Get Card Details**: Retrieve the details of an existing card.
- **Block/Unblock Card**: Block or unblock a card by providing its ID.
- **Withdraw Funds**: Withdraw funds from a card, with support for currency conversion.
- **Top Up Funds**: Top up funds to a card, with support for currency conversion.
- **Transaction History**: Retrieve the transaction history of a card with optional filtering.

## Technologies Used

- **Java 17**
- **Spring Boot 3**
- **PostgreSQL**
- **Redis** (for caching)
- **Maven** (for dependency management)
- **Docker** (for containerization)
- **Swagger** (for API documentation)

## Getting Started

### Prerequisites

Before you start, ensure you have the following installed:

- **Docker Desktop**: Required to run the application in a containerized environment.
- **Users**: As initial data, there will be two users with emails as `admin@gmail.com` in role of `ROLE_ADMIN` and `client@gmail.com` in role of `ROLE_CLIENT`. <br>**ONLY ADMIN CAN ACCESS TO API apart from auth APIs**

### Installation

1. **Download the [docker-compose.yml](docker-compose.yml) file located in the repository.**
2. **Start the application:**
   <br> Navigate to the directory containing the docker-compose.yml file and run the following command:
   ```bash
   docker compose up -d
   ```
   This command will start the application and its dependencies in detached mode.
3. **Access the APIs:**
   <br> Open your browser and navigate to the following URL to view the API documentation:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```
   All available APIs will be listed here.

## Stopping the Application

After using the application, you can stop the Docker container in one of the following ways:

- **Using Docker Desktop:** Open Docker Desktop and stop the running container.
- **Using the command line:**
    1. List all running containers:

    ```bash
    docker ps
    ```
  
    2. Note the CONTAINER ID of the running application container and stop it using the following command:
    ```bash
    docker stop <container_id>
    ```

## API Endpoints
The following are the main API endpoints provided by this service:
1. **Sign up as a client: `POST /api/v1/auth/sign_up`**
2. **Login: `POST /api/v1/auth/login`**
3. **Create New Card: `POST /api/v1/cards`**
4. **Get Card Details: `GET /api/v1/cards/{cardId}`**
5. **Block Card: `POST /api/v1/cards/{cardId}/block`**
6. **Unblock Card: `POST /api/v1/cards/{cardId}/unblock`**
7. **Withdraw Funds: `POST /api/v1/cards/{cardId}/debit`**
8. **Top Up Funds: `POST /api/v1/cards/{cardId}/credit`**
9. **Get Transaction History: `GET /api/v1/cards/{cardId}/transactions`**

<br>For detailed request and response examples, please refer to the Swagger documentation accessible via the provided URL.