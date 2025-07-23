FROM eclipse-temurin:21-jdk-ubi9-minimal
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

# COMMANDS TO RUN
# docker build -t user-service ./user
# docker run --env-file configserver/.env -d --name configserver -p 8888:8888 configserver

# DEBUGING COMMANDS
# docker exec configserver ls /config