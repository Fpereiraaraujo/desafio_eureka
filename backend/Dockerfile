FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw -B -DskipTests package


FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/cooperfilme-processor-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]