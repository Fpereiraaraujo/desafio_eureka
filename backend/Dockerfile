# ===== Etapa 1: Build =====
FROM maven:3.9.3 AS build
WORKDIR /app

# Copia o pom.xml
COPY pom.xml ./

# Copia o código fonte
COPY src ./src

# Build do JAR
RUN mvn clean package -DskipTests

# ===== Etapa 2: Runtime =====
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
