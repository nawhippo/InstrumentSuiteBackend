# Build stage
FROM maven:3.8.4-openjdk-17 as builder

WORKDIR /app

COPY src /app/src
COPY pom.xml /app

RUN mvn -f /app/pom.xml clean package

# Final stage
FROM eclipse-temurin:17-jre

# Update and install FFmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy the built jar file from the build stage
COPY --from=builder /app/target/*.jar /app.jar

# Set the entry point to run your application
ENTRYPOINT ["sh", "-c", "env && java -Dserver.port=$PORT -jar /app.jar"]