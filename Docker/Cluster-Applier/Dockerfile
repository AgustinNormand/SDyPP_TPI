FROM openjdk:latest
COPY Cluster-Applier.jar server.jar

COPY credentials.json /root/
COPY kubeconfig.yaml /root/

ENV GOOGLE_APPLICATION_CREDENTIALS="/root/credentials.json"
ENV KUBECONFIG="/root/kubeconfig.yaml"


ENTRYPOINT ["java", "-jar", "server.jar"]