FROM openjdk:17-jdk-slim

WORKDIR /app

COPY /target/*.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]