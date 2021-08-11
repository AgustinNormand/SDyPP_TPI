#!/bin/bash

get_value () {
    echo $(cat variables.tf | grep $1 -A 4 | grep default | awk {'print $3'} | sed 's/"//g') 
}

rm ~/.kube/config

gcloud container clusters get-credentials $(get_value "management_cluster_name") --region $(get_value "management_region")

gcloud container clusters get-credentials $(get_value "deployments_cluster_name") --region $(get_value "deployments_region")

gcloud container clusters get-credentials $(get_value "resources_cluster_name") --region $(get_value "resources_region")
