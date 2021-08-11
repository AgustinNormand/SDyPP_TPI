#!/bin/bash

get_value () {
    echo $(cat variables.tf | grep $1 -A 4 | grep default | awk {'print $3'} | sed 's/"//g') 
}

gcloud iam service-accounts keys create ./credentials.json --iam-account=$(get_service_account_name $(get_value "management_nodes_sa_name"))

gcloud auth activate-service-account $(get_service_account_name $(get_value "management_nodes_sa_name")) --key-file=./credentials.json

rm ~/.kube/config

gcloud container clusters get-credentials $(get_value "management_cluster_name") --region $(get_value "management_region")

gcloud container clusters get-credentials $(get_value "deployments_cluster_name") --region $(get_value "deployments_region")

gcloud container clusters get-credentials $(get_value "resources_cluster_name") --region $(get_value "resources_region")
