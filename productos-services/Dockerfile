# Etapa 1: construir con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY ../ .
WORKDIR /app/productos-services
RUN mvn clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/productos-services/target/productos-services-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
