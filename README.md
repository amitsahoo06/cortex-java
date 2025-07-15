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

```bash
mvn spring-boot:run
```

The application will start on port 12000 by default. You can access the API at http://localhost:12000/api.

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

## Configuration

The application can be configured through the `application.properties` file in the `src/main/resources` directory.

## License

This project is licensed under the MIT License - see the LICENSE file for details.