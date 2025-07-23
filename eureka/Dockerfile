FROM eclipse-temurin:21-jdk-ubi9-minimal
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]

# COMMANDS TO RUN
# docker build -t eureka ./eureka
# docker run --env-file configserver/.env -d --name configserver -p 8888:8888 configserver