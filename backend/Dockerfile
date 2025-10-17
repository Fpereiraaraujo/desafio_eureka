# Usamos uma imagem leve com Java 17
FROM openjdk:17-jdk-slim

# Diretório de trabalho dentro do container
WORKDIR /app

# Copiamos o JAR gerado pelo Maven para dentro do container
# Certifique-se de rodar "mvn clean package" antes
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Porta que o Spring Boot vai expor
EXPOSE 8080

# Comando para rodar o Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
