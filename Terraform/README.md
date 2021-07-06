# Learn Terraform - Provision a GKE Cluster

[Provision a GKE Cluster learn guide](https://learn.hashicorp.com/terraform/kubernetes/provision-gke-cluster)

# Pasos para crear un cluster de Kubernetes en Google Cloud
* terraform init
* terraform apply -auto-approve
* Crear el archivo kube config: gcloud container clusters get-credentials $(terraform output -raw kubernetes_cluster_name) --region $(terraform output -raw region)
