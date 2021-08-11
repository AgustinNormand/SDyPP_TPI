#!/bin/bash

get_value () {
    echo $(cat variables.tf | grep $1 -A 4 | grep default | awk {'print $3'} | sed 's/"//g') 
}

get_billing_account () {
    echo $(gcloud alpha billing accounts list | awk {'print $1'} | tail -n 1)
}

get_service_account_name() {
    echo $1'@'$(get_value "project_id")'.iam.gserviceaccount.com'
}

instalar_argo(){
    kubectl create namespace argocd
    kubectl apply -n argocd -f install.yaml
    kubectl apply -n argocd -f ../ArgoCD/$1
}

instalar_external_dns(){
    kubectl create namespace kubernetes-sa
    kubectl create serviceaccount --namespace kubernetes-sa service-account
    gcloud iam service-accounts add-iam-policy-binding --role roles/iam.workloadIdentityUser --member "serviceAccount:"$(get_value "project_id")".svc.id.goog[kubernetes-sa/service-account]" $(get_service_account_name $(get_value "service_account"))
    kubectl annotate serviceaccount --namespace kubernetes-sa service-account iam.gke.io/gcp-service-account=$(get_service_account_name $(get_value "service_account"))
    DNS_ZONE_DOMAIN=$(get_value 'dns_zone_domain' | sed 's/.$//')
    PROJECT_ID=$(get_value 'project_id')
    cd ../ExternalDNS/
    cat external-dns.yaml | sed "s/DOMAIN/$DNS_ZONE_DOMAIN/g" | sed "s/PROJECT/$PROJECT_ID/g">newexternal.yaml
    mv newexternal.yaml ../Kubernetes/Resources/external-dns.yaml
    cd -
}

replace_domain_in_resources_yaml(){
    DNS_ZONE_DOMAIN=$(get_value 'dns_zone_domain')
    cd ../Kubernetes/Resources/
    cat 02-rabbitmq-service.yaml | sed "s/DOMAIN/$DNS_ZONE_DOMAIN/g">02-rabbitmq-service.yaml.new
    mv 02-rabbitmq-service.yaml.new 02-rabbitmq-service.yaml

    cat 04-redis-service.yaml | sed "s/DOMAIN/$DNS_ZONE_DOMAIN/g">04-redis-service.yaml.new
    mv 04-redis-service.yaml.new 04-redis-service.yaml
    cd -
}

commit_changes(){
    git add ../Docker/Cluster-Applier/kubeconfig.yaml
    git add ../Kubernetes/Resources/external-dns.yaml
    #git add ../Kubernetes/Resources/.

    git commit -m "Updated deployment files"

    git pull origin main

    git push origin main
}

#gcloud auth login

#gcloud projects create $(get_value "project_id") --name=$(get_value "project_name") --set-as-default

#gcloud config set project $(get_value "project_id")

#gcloud alpha billing projects link $(get_value "project_id") --billing-account $(get_billing_account)

#gcloud services enable compute.googleapis.com

#gcloud services enable container.googleapis.com

#gcloud services enable dns.googleapis.com

#gcloud services enable cloudresourcemanager.googleapis.com 

#gcloud iam service-accounts create $(get_value "management_nodes_sa_name")

#gcloud iam service-accounts create $(get_value "service_account")

#gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "service_account")) --role=roles/owner

#gcloud iam service-accounts keys create ./credentials.json --iam-account=$(get_service_account_name $(get_value "management_nodes_sa_name"))

#gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/owner

#gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/iam.serviceAccountUser

#gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/storage.admin

#gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/dns.admin

#gcloud auth activate-service-account $(get_service_account_name $(get_value "management_nodes_sa_name")) --key-file=./credentials.json

#export GOOGLE_APPLICATION_CREDENTIALS=$(pwd)'/credentials.json'

#terraform init

#terraform apply --auto-approve

#gh secret set GOOGLE_CREDENTIALS < credentials.json

gcloud container clusters get-credentials $(get_value "deployments_cluster_name") --region $(get_value "deployments_region")

export GET_CMD="gcloud container clusters describe $(get_value "deployments_cluster_name") --region=$(get_value "deployments_region")"

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

mv kubeconfig.yaml ../Docker/Cluster-Applier/kubeconfig.yaml

wget https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

instalar_argo "Deployments"

rm ~/.kube/config

gcloud container clusters get-credentials $(get_value "resources_cluster_name") --region $(get_value "resources_region")

instalar_argo "Resources"

instalar_external_dns

#replace_domain_in_resources_yaml

rm ~/.kube/config

gcloud container clusters get-credentials $(get_value "management_cluster_name") --region $(get_value "management_region")

instalar_argo "Management"

rm install.yaml

commit_changes

gcloud container clusters get-credentials $(get_value "deployments_cluster_name") --region $(get_value "deployments_region")

gcloud container clusters get-credentials $(get_value "resources_cluster_name") --region $(get_value "resources_region")
