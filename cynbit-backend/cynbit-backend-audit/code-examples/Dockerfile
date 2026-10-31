FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY cynbit-backend/cynbit-backend-audit/code-examples/pom.xml ./pom.xml
COPY cynbit-backend/cynbit-backend-audit/code-examples/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV PORT=10000
EXPOSE 10000
CMD java -Dserver.port=$PORT -jar app.jar
