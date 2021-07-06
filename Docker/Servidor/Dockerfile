FROM agustinnormand/tpi_javakubectl:0.10

COPY Server.jar /usr/src/
COPY kubeconfig.yaml /root/
COPY credentials.json /root/

ENV GOOGLE_APPLICATION_CREDENTIALS="/root/credentials.json"
ENV KUBECONFIG="/root/kubeconfig.yaml"

EXPOSE 8080

WORKDIR /usr/src/
ENTRYPOINT ["java", "-jar", "Server.jar"]
