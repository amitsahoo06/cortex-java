# CortexON Java Spring Boot API

This is a Java Spring Boot implementation of the CortexON API, which provides a backend for orchestrating AI agents. This project is a port of the Python CortexON project to Java Spring Boot.

## Features

- RESTful API for agent interactions
- WebSocket support for real-time communication
- Swagger UI for API documentation and testing
- Modular architecture with service interfaces and implementations

## Prerequisites

- Java 17 or higher
- Maven 3.8 or higher

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/cortexon-java.git
cd cortexon-java
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

#### Backend Only

```bash
mvn spring-boot:run
```

The backend will start on port 12000 by default. You can access the API at http://localhost:12000/api.

#### Frontend Only

```bash
cd frontend
npm install
npm run dev
```

The frontend will start on port 12001 by default. You can access the UI at http://localhost:12001.

#### Run Both Together

We provide a convenience script to start both the backend and frontend:

```bash
./start.sh
```

This will start the backend on port 12000 and the frontend on port 12001.

## API Documentation

Swagger UI is available at http://localhost:12000/api/swagger-ui.html

## Project Structure

- `src/main/java/com/cortexon/api`: Main source code
  - `controller`: REST controllers
  - `model`: Data models
  - `service`: Service interfaces and implementations
  - `config`: Configuration classes
  - `util`: Utility classes
- `src/main/resources`: Configuration files
- `src/test`: Test code
- `frontend`: React frontend application
  - `src`: Frontend source code
  - `public`: Static assets

## Frontend-Backend Integration

The frontend communicates with the backend through:

1. **REST API**: For standard HTTP requests
2. **WebSocket**: For real-time streaming responses

The integration is configured in the following files:

- Backend:
  - `WebSocketConfig.java`: Configures WebSocket endpoints
  - `CorsConfig.java`: Configures CORS to allow frontend requests
  - `WebSocketController.java`: Handles WebSocket connections

- Frontend:
  - `.env`: Contains API and WebSocket URLs
  - `src/services/platformConfig.ts`: Configures API base URL
  - `src/components/home/ChatList.tsx`: Handles WebSocket communication

## Configuration

The application can be configured through the `application.properties` file in the `src/main/resources` directory.

## License

This project is licensed under the MIT License - see the LICENSE file for details.