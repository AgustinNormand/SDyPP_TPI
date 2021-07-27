FROM openjdk:latest
COPY Yaml-Manager.jar server.jar

COPY credentials.json /usr/src/google_credentials/credentials.json

ENTRYPOINT ["java", "-jar", "server.jar"]