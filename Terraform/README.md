# Learn Terraform - Provision a GKE Cluster

[Provision a GKE Cluster learn guide](https://learn.hashicorp.com/terraform/kubernetes/provision-gke-cluster)

# Pasos para crear un cluster de Kubernetes en Google Cloud
* Crear una cuenta gratuita, ingresando el numero de tarjeta.
* Crear un archivo de credenciales (IAM y Administracion -> Cuentas de Servicio -> Crear cuenta de Servicio -> Claves -> Agregar Clave -> Crear clave nueva -> JSON)
* Habilitar Kubernetes Engine API (https://console.cloud.google.com/marketplace/product/google/container.googleapis.com)
* terraform init
* terraform apply -auto-approve
* Logearse en gcloud: glcoud auth login
* Crear el archivo kube config: gcloud container clusters get-credentials $(terraform output -raw kubernetes_cluster_name) --region $(terraform output -raw region)

