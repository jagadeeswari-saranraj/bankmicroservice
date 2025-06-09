# Start with base image containing java runtime
FROM openjdk:25-ea-21-jdk-slim-bullseye

# Information cround who maintains the image
MAINTAINER bank.com

# Add the application's jar to the image
COPY target/microservice_accounts-0.0.1-SNAPSHOT.jar microservice_accounts-0.0.1-SNAPSHOT.jar

EXPOSE 8080

#execute the application
ENTRYPOINT ["java", "-jar", "microservice_accounts-0.0.1-SNAPSHOT.jar"]