
FROM maven:3.8.4-openjdk-17 as builder

WORKDIR /app

COPY src /app/src
COPY pom.xml /app

RUN mvn -f /app/pom.xml clean package


FROM eclipse-temurin:17-jre

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["sh", "-c", "env && java -Dserver.port=$PORT -jar /app.jar"]