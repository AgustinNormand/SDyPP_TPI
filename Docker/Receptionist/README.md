# Deploy
* Place here credentials.json, config, kubeconfig.yaml

# Steps done
* Cluster GKE levantado
* Archivo credenciales GKE

* Obtener el nombre y la zona del cluster (Ej: "sdypp-316414-gke" y "southamerica-east1")
* Reemplazar el nombre y la zona del cluster, y setear la siguiente variable de entorno: 
``` 
GET_CMD="gcloud container clusters describe [CLUSTER] --zone=[ZONE]" 
```

Ej: 
``` 
GET_CMD="gcloud container clusters describe gke-deployments-cluster --region=us-central1"
```

* Generar el archivo kubeconfig.yaml
``` 
cat > kubeconfig.yaml <<EOF
apiVersion: v1
kind: Config
current-context: my-cluster
contexts: [{name: my-cluster, context: {cluster: cluster-1, user: user-1}}]
users: [{name: user-1, user: {auth-provider: {name: gcp}}}]
clusters:
- name: cluster-1
  cluster:
    server: "https://$(eval "$GET_CMD --format='value(endpoint)'")"
    certificate-authority-data: "$(eval "$GET_CMD --format='value(masterAuth.clusterCaCertificate)'")"
EOF
```   
* Setear las siguientes varaibles de entorno con el path a kubeconfig.yaml y al archivo de credenciales de GCP
```
export GOOGLE_APPLICATION_CREDENTIALS=service-account-key.json
export KUBECONFIG=kubeconfig.yaml
```

# References
* https://ahmet.im/blog/authenticating-to-gke-without-gcloud/
