# Build the docker image
# docker build -f src/main/docker/Dockerfile -t quarkus-gradle-app .
#
# Run the docker container
# docker run -i --rm -p 8080:8080 quarkus-gradle-app
#
# You can specify the remote website you want to test (for example quarkus.io)
# by passing the env parameter to docker container like this:

# docker run -e REMOTE_BASE_URI="quarkus.io" -i --rm -p 8080:8080 quarkus-gradle-app

# Here REMOTE_BASE_URI="quarkus.io" is the desired website to proxy

# Stage 1: Build the application using Gradle
FROM gradle:8.9.0-jdk21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy only the necessary files first (to take advantage of Docker layer caching)
COPY build.gradle.kts settings.gradle.kts /app/

# Download Gradle dependencies (this will be cached if files haven't changed)
RUN gradle build -i --no-daemon || return 0

# Copy the rest of the application source code
COPY . /app

# Build the Quarkus application
RUN gradle quarkusBuild --no-daemon

# Stage 2: Create a minimal image to run the Quarkus application
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built application from the previous stage
COPY --from=build /app/build/quarkus-app /app/

# Expose the application port (default Quarkus HTTP port)
EXPOSE 8080

# Run the Quarkus application
CMD ["java", "-jar", "/app/quarkus-run.jar"]