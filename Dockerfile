FROM openjdk:11-jre-slim-buster
ARG JAR_FILE=build/libs/diagnosticMicroservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} diagnosticMicroservice.jar
ENTRYPOINT ["java","-jar","diagnosticMicroservice.jar"]
EXPOSE 8083