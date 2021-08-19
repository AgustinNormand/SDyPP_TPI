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

gcloud auth login

gcloud projects create $(get_value "project_id") --name=$(get_value "project_name") --set-as-default

gcloud config set project $(get_value "project_id")

gcloud alpha billing projects link $(get_value "project_id") --billing-account $(get_billing_account)

gcloud services enable compute.googleapis.com

gcloud services enable container.googleapis.com

gcloud services enable dns.googleapis.com

gcloud services enable cloudresourcemanager.googleapis.com 

gcloud iam service-accounts create $(get_value "management_nodes_sa_name")

gcloud iam service-accounts create $(get_value "service_account")

gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "service_account")) --role=roles/owner

gcloud iam service-accounts keys create ./credentials.json --iam-account=$(get_service_account_name $(get_value "management_nodes_sa_name"))

gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/owner

gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/iam.serviceAccountUser

gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/storage.admin

gcloud projects add-iam-policy-binding $(get_value "project_id") --member="serviceAccount:"$(get_service_account_name $(get_value "management_nodes_sa_name")) --role=roles/dns.admin

gcloud auth activate-service-account $(get_service_account_name $(get_value "management_nodes_sa_name")) --key-file=./credentials.json
