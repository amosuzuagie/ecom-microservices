#!/bin/bash

cd ../..

# Building All Project commands
cd configserver && ./mvnw clean package -DskipTests && cd ..
cd notification && ./mvnw clean package -DskipTests && cd ..
cd product && ./mvnw clean package -DskipTests && cd ..
cd gateway && ./mvnw clean package -DskipTests && cd ..
cd eureka && ./mvnw clean package -DskipTests && cd ..
cd order && ./mvnw clean package -DskipTests && cd ..
cd user && ./mvnw clean package -DskipTests && cd ..

# Debugging on mac and linux
# chmod x start-database.sh
# this ensures the script is executable and can be run with: ./<file-name>.sh