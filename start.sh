#!/bin/bash

# Start the Spring Boot backend
echo "Starting Spring Boot backend..."
cd /workspace/cortex-java/cortexon-java
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.address=0.0.0.0 -Dserver.port=12000" &
BACKEND_PID=$!

# Wait for backend to start
echo "Waiting for backend to start..."
sleep 10

# Start the frontend
echo "Starting frontend..."
cd /workspace/cortex-java/cortexon-java/frontend
npm install
npm run dev &
FRONTEND_PID=$!

# Function to handle script termination
cleanup() {
  echo "Stopping services..."
  kill $FRONTEND_PID
  kill $BACKEND_PID
  exit 0
}

# Register the cleanup function for script termination
trap cleanup SIGINT SIGTERM

# Keep the script running
echo "Services started. Press Ctrl+C to stop."
wait