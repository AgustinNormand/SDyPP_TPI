FROM openjdk:latest
COPY Entrypoint.jar server.jar

COPY credentials.json /usr/src/google_credentials/credentials.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server.jar"]