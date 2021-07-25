variable "project_name" {
  description = ""
 type        = string
 default     = "sdypp-automated-5"
}

variable "project_id" {
  description = ""
  type        = string
  default     = "sdypp-automated-5"
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

variable "region" {
  description = ""
  type        = string
  default     = "us-central1"
}

variable "service_account" {
  description = ""
  type        = string
  default     = "serviceaccount@sdypp-316414.iam.gserviceaccount.com"
}

variable "node_locations" {
  description = ""
  type        = string
  default     = "us-central1-b,us-central1-c"
}

variable "machine_type" {
  description = ""
  type        = string
  default     = "g1-small"
}

variable "deployments_cluster_name" {
  description = ""
  type        = string
  default     = "gke-deployments-cluster"
}

variable "zone1" {
  description = ""
  type        = string
  default     = "us-central1-a"
}


variable "zone2" {
  description = ""
  type        = string
  default     = "us-central1-b"
}


variable "zone3" {
  description = ""
  type        = string
  default     = "us-central1-f"
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


