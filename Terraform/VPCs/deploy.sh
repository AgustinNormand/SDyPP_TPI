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
    cd ../../ArgoCD/$1
    kubectl apply -n argocd -f app.yaml
    cd -
}

#echo "First, login and accept Terms of Service"

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

#gcloud projects add-iam-policy-binding sdypp-framework --member=$(get_service_account_name $(get_value "service_account")) --role=roles/owner

#gcloud iam service-accounts keys create ./credentials.json --iam-account=$(get_service_account_name $(get_value "management_nodes_sa_name"))

#gcloud projects add-iam-policy-binding sdypp-framework --member=serviceAccount:$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/owner

#gcloud projects add-iam-policy-binding sdypp-framework --member=serviceAccount:$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/iam.serviceAccountUser

#gcloud projects add-iam-policy-binding sdypp-framework --member=serviceAccount:$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/dns.admin

#gcloud auth activate-service-account $(get_service_account_name $(get_value "management_nodes_sa_name")) --key-file=./credentials.json

export GOOGLE_APPLICATION_CREDENTIALS=$(pwd)'/credentials.json'

#terraform init

#terraform apply --auto-approve

#gh secret set GOOGLE_CREDENTIALS --body=$(echo credentials.json)

#gcloud container clusters get-credentials $(get_value "deployments_cluster_name") --region $(get_value "deployments_region")

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

mv kubeconfig.yaml ../../Docker/Receptionist/kubeconfig.yaml

#wget https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

#instalar_argo "Deployments"

#rm ~/.kube/config

#gcloud container clusters get-credentials $(get_value "resources_cluster_name") --region $(get_value "resources_region")

#instalar_argo "Resources"

rm ~/.kube/config

gcloud container clusters get-credentials $(get_value "management_cluster_name") --region $(get_value "management_region")

instalar_argo "Management"

git add ../../Docker/Receptionist/kubeconfig.yaml

git commit -m "Updated kubeconfig.yaml with new cluster credentials."

git pull origin main

git push origin main