variable "project_name" {
  description = ""
  type        = string
  default     = "sdypp-framework"
}

variable "project_id" {
  description = ""
  type        = string
  default     = "sdypp-framework"
}

variable "dns_zone_name" {
  description = ""
  type        = string
  default     = "framework-services-gcp-com-ar"
}

variable "dns_zone_domain" {
  description = "Siempre terminado con ."
  type        = string
  default     = "framework.services.gcp.com.ar."
}

variable "management_nodes_sa_name" {
  description = ""
  type        = string
  default     = "management-nodes-sa"
}

variable "deployments_vpc_identifier" {
  description = ""
  type        = string
  default     = "deployments"
}

variable "resources_vpc_identifier" {
  description = ""
  type        = string
  default     = "resources"
}

variable "management_vpc_identifier" {
  description = ""
  type        = string
  default     = "management"
}

variable "deployments_region" {
  description = ""
  type        = string
  default     = "us-central1"
}

#variable "deployments_zone1" {
#  description = ""
#  type        = string
#  default     = "us-central1-a"
#}

#variable "deployments_zone2" {
 # description = ""
 # type        = string
#  default     = "us-central1-b"
#}

variable "deployments_node_locations" {
  description = ""
  type        = string
  default     = "us-central1-a"
}

variable "management_region" {
  description = ""
  type        = string
  default     = "us-east1"
}

#variable "management_zone1" {
 # description = ""
 # type        = string
 # default     = "us-east1-b"
#}

#variable "management_zone2" {
#  description = ""
#  type        = string
#  default     = "us-east1-c"
#}

variable "management_node_locations" {
  description = ""
  type        = string
  default     = "us-east1-b"
}

variable "resources_region" {
  description = ""
  type        = string
  default     = "us-west1"
}

#variable "resources_zone1" {
#  description = ""
#  type        = string
#  default     = "us-west1-a"
#}

#variable "resources_zone2" {
#  description = ""
#  type        = string
#  default     = "us-west1-b"
#}

variable "resources_node_locations" {
  description = ""
  type        = string
  default     = "us-west1-a"
}

variable "service_account" {
  description = ""
  type        = string
  default     = "serviceaccount"
}

variable "machine_type" {
  description = ""
  type        = string
  default     = "n1-standard-1"
}

variable "deployments_cluster_name" {
  description = ""
  type        = string
  default     = "gke-deployments-cluster"
}

variable "resources_cluster_name" {
  description = ""
  type        = string
  default     = "gke-resources-cluster"
}

variable "management_cluster_name" {
  description = ""
  type        = string
  default     = "gke-management-cluster"
}

variable "deployments_min_cpu" {
  description = ""
  type        = number
  default     = 1
}

variable "deployments_max_cpu" {
  description = ""
  type        = number
  default     = 10
}

variable "deployments_min_mem" {
  description = ""
  type        = number
  default     = 1
}

variable "deployments_max_mem" {
  description = ""
  type        = number
  default     = 64
}

variable "resources_min_cpu" {
  description = ""
  type        = number
  default     = 1
}

variable "resources_max_cpu" {
  description = ""
  type        = number
  default     = 10
}

variable "resources_min_mem" {
  description = ""
  type        = number
  default     = 1
}

variable "resources_max_mem" {
  description = ""
  type        = number
  default     = 64
}

variable "management_min_cpu" {
  description = ""
  type        = number
  default     = 1
}

variable "management_max_cpu" {
  description = ""
  type        = number
  default     = 10
}

variable "management_min_mem" {
  description = ""
  type        = number
  default     = 1
}

variable "management_max_mem" {
  description = ""
  type        = number
  default     = 64
}

