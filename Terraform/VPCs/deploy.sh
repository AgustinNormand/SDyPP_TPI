#!/bin/bash

get_value () {
    echo $(cat variables.tf | grep $1 -A 4 | grep default | awk {'print $3'} | sed 's/"//g') 
}

get_billing_account () {
    echo $(gcloud alpha billing accounts list | awk {'print $1'} | tail -n 1)
}

#gcloud projects create $(get_value "project_id") --name=$(get_value "project_name") --set-as-default

gcloud config set project $(get_value "project_id")

#gcloud alpha billing projects link $(get_value "project_id") --billing-account $(get_billing_account)

gcloud services enable compute.googleapis.com

terraform init

terraform apply --auto-approve