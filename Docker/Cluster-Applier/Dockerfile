FROM agustinnormand/tpi_javakubectl:0.10
COPY Cluster-Applier.jar server.jar

COPY credentials.json /usr/src/google_credentials/credentials.json
COPY kubeconfig.yaml /root/

ENV GOOGLE_APPLICATION_CREDENTIALS="/usr/src/google_credentials/credentials.json"
ENV KUBECONFIG="/root/kubeconfig.yaml"


ENTRYPOINT ["java", "-jar", "server.jar"] 