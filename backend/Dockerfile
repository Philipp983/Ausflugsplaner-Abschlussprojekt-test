FROM openjdk:17-alpine
LABEL authors="Eva, Osama, Mazlum & Philipp"

# set working directory
WORKDIR /spring-app
COPY target/backend-0.0.1-SNAPSHOT.jar /spring-app/backend-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-Dserver.port=${PORT:8080}", "-jar", "/spring-app/backend-0.0.1-SNAPSHOT.jar"]
